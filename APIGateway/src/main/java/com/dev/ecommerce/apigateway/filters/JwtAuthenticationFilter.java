package com.dev.ecommerce.apigateway.filters;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Component
public class JwtAuthenticationFilter implements GlobalFilter {

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if (exchange.getRequest().getMethod().name().equals("OPTIONS")) {
            return chain.filter(exchange);
        }
        // Skip public endpoints
        String path = exchange.getRequest().getURI().getPath();
        if (path.contains("/auth/login") || path.contains("/auth/signup") || path.contains("/auth/logout") ||
            path.contains("/api/stripewebhook") ||
            path.contains("/products") && exchange.getRequest().getMethod().name().equals("GET") ||
            path.contains("/search") && exchange.getRequest().getMethod().name().equals("GET")) {
            return chain.filter(exchange);
        }

        // Extract token from COOKIE (not header)
        HttpCookie cookie = exchange.getRequest()
                .getCookies()
                .getFirst("token");

        if (cookie == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = cookie.getValue();

        try {
            // VALIDATION
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}
