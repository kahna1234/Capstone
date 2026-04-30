package com.dev.ecommerce.apigateway.security;

import com.dev.ecommerce.apigateway.config.SecurityConstants;
import org.springframework.http.HttpCookie;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class JwtSecurityContextRepository implements ServerSecurityContextRepository {

    private final JwtAuthenticationManager authenticationManager;

    public JwtSecurityContextRepository(JwtAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {

        String path = exchange.getRequest().getPath().value();

        for (String publicEndpoint : SecurityConstants.PUBLIC_ENDPOINTS) {
            String pattern = publicEndpoint.endsWith("/**") 
                    ? publicEndpoint.substring(0, publicEndpoint.length() - 3) 
                    : publicEndpoint;
            if (path.equals(pattern) || path.startsWith(pattern + "/")) {
                return Mono.empty();
            }
        }

        // ✅ 1. Check Authorization header
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization");

        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        // ✅ 2. Fallback to cookie
        if (token == null) {
            HttpCookie cookie = exchange.getRequest().getCookies().getFirst("token");
            if (cookie != null) {
                token = cookie.getValue();
            }
        }

        if (token == null) {
            return Mono.empty();
        }

        JwtAuthenticationToken authToken = new JwtAuthenticationToken(token);

        return authenticationManager.authenticate(authToken)
                .map(SecurityContextImpl::new);
    }
}
