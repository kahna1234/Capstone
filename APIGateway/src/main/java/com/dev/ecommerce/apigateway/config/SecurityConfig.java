package com.dev.ecommerce.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    
    // Public endpoints that don't require authentication
    private static final String[] PUBLIC_ENDPOINTS = {
        "/auth/login",
        "/auth/signup",
        "/auth/logout",
        "/api/stripewebhook",
        "/products",
        "/search",
        "/orders/**",
        "/orders/*/status/webhook",
        "/cart/**",
        "/api/payments/**"
    };
    
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(csrf -> csrf
                .disable() // Disabled for now as cookies use HttpOnly and SameSite=Lax
                // To enable CSRF, use: .csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
            )
            .authorizeExchange(auth -> auth
                // Allow OPTIONS requests for CORS preflight
                .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Allow public endpoints
                .pathMatchers(PUBLIC_ENDPOINTS).permitAll()
                // Require authentication for all other requests
                .anyExchange().authenticated()
            );
        return http.build();
    }
    
    // CSRF token filter for state-changing operations
    @Bean
    public WebFilter csrfWebFilter() {
        return (exchange, chain) -> {
            Mono<CsrfToken> csrfTokenMono = exchange.getAttribute(CsrfToken.class.getName());
            if (csrfTokenMono != null) {
                return csrfTokenMono.doOnSuccess(csrfToken -> {
                    // Add CSRF token to response headers
                    exchange.getResponse().getHeaders().add("X-CSRF-TOKEN", csrfToken.getToken());
                }).then(chain.filter(exchange));
            }
            return chain.filter(exchange);
        };
    }
}

