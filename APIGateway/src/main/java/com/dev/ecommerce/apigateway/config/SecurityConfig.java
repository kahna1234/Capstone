package com.dev.ecommerce.apigateway.config;

import com.dev.ecommerce.apigateway.security.JwtAuthenticationManager;
import com.dev.ecommerce.apigateway.security.JwtSecurityContextRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public JwtAuthenticationManager jwtAuthenticationManager() {
        return new JwtAuthenticationManager(jwtSecret);
    }

    @Bean
    public JwtSecurityContextRepository jwtSecurityContextRepository(
            JwtAuthenticationManager manager) {
        return new JwtSecurityContextRepository(manager);
    }

    
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         JwtAuthenticationManager authManager,
                                                         JwtSecurityContextRepository contextRepo) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authenticationManager(authManager)
                .securityContextRepository(contextRepo)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.OPTIONS)
                        .permitAll()
                        .pathMatchers(SecurityConstants.PUBLIC_ENDPOINTS.toArray(new String[0]))
                        .permitAll()
                        .anyExchange().authenticated()
                )
                .build();
    }
}