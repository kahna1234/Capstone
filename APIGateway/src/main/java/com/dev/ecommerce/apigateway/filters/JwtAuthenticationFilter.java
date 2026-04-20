package com.dev.ecommerce.apigateway.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter {

    @Value("${jwt.secret}")
    private String secret;

    // Public endpoints that don't require authentication
    private static final List<String> PUBLIC_ENDPOINTS = List.of(
        "/auth/login",
        "/auth/signup",
        "/auth/logout",
        "/api/stripewebhook",
        "/orders",
        "/cart",
        "/api/payments"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if (exchange.getRequest().getMethod().name().equals("OPTIONS")) {
            return chain.filter(exchange);
        }

        String path = exchange.getRequest().getURI().getPath();

        // Skip public endpoints
        if (PUBLIC_ENDPOINTS.stream().anyMatch(path::contains) ||
            (path.contains("/products") && exchange.getRequest().getMethod().name().equals("GET")) ||
            (path.contains("/search") && exchange.getRequest().getMethod().name().equals("GET"))) {
            return chain.filter(exchange);
        }

        // Extract token from COOKIE
        HttpCookie cookie = exchange.getRequest()
                .getCookies()
                .getFirst("token");

        if (cookie == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = cookie.getValue();

        try {
            // Validate token and extract claims
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Add user claims to request headers for downstream services
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", claims.getSubject())
                    .header("X-User-Email", claims.get("email", String.class))
                    .header("X-User-Roles", claims.get("roles", String.class))
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}
