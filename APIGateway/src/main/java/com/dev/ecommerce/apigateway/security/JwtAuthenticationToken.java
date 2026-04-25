package com.dev.ecommerce.apigateway.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;

    public JwtAuthenticationToken(String token) {
        super(null);
        this.token = token;
        setAuthenticated(false);
    }

    public String getCredentials() {
        return token;
    }

    public Object getPrincipal() {
        return null;
    }
}