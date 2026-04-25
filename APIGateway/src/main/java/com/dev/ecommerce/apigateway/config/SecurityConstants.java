package com.dev.ecommerce.apigateway.config;

import java.util.List;

public class SecurityConstants {

    public static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/auth/login",
            "/auth/signup",
            "/auth/logout",
            "/api/stripewebhook",
            "/products/**",
            "/search/**",
            "/orders",
            "/orders/**",
            "/inventory/**",
            "/cart/**"
    );
}
