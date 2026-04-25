package com.dev.ecommerce.apigateway.security;

import com.dev.ecommerce.apigateway.config.SecurityConstants;
import org.springframework.http.HttpCookie;
import org.springframework.security.core.context.SecurityContext;
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
        
        // Skip JWT validation for public endpoints
        for (String publicEndpoint : SecurityConstants.PUBLIC_ENDPOINTS) {
            if (path.startsWith(publicEndpoint.replace("/**", ""))) {
                return Mono.empty();
            }
        }

        HttpCookie cookie = exchange.getRequest().getCookies().getFirst("token");

        if (cookie == null) {
            return Mono.empty();
        }

        JwtAuthenticationToken authToken = new JwtAuthenticationToken(cookie.getValue());

        return authenticationManager.authenticate(authToken)
                .map(auth -> new org.springframework.security.core.context.SecurityContextImpl(auth));
    }
}
