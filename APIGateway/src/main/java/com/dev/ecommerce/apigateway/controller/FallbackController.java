package com.dev.ecommerce.apigateway.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @RequestMapping("/products")
    public Map<String, String> productFallback() {
        return Map.of("error", "Product service unavailable");
    }

    @RequestMapping("/orders")
    public Map<String, String> orderFallback() {
        return Map.of("error", "Order service unavailable");
    }

    @RequestMapping("/payments")
    public Map<String, String> paymentFallback() {
        return Map.of("error", "Payment service unavailable");
    }

    @RequestMapping("/auth")
    public Map<String, String> authFallback() {
        return Map.of("error", "Auth service unavailable");
    }
}