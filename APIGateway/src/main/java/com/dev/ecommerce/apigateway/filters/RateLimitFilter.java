package com.dev.ecommerce.apigateway.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitFilter implements GlobalFilter, Ordered {

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    private static final int RATE_LIMIT = 100; // requests per minute
    private static final int RATE_LIMIT_WINDOW = 60; // seconds

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String clientIp = getClientIp(exchange.getRequest());
        String key = "rate_limit:" + clientIp;

        return redisTemplate.opsForValue()
                .increment(key)
                .flatMap(count -> {
                    if (count == 1) {
                        // Set expiration for first request
                        return redisTemplate.expire(key, Duration.ofSeconds(RATE_LIMIT_WINDOW))
                                .then(Mono.just(count));
                    }
                    return Mono.just(count);
                })
                .flatMap(count -> {
                    if (count > RATE_LIMIT) {
                        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                        return exchange.getResponse().setComplete();
                    }
                    return chain.filter(exchange);
                })
                .onErrorResume(e -> chain.filter(exchange)); // Fail open if Redis is down
    }

    private String getClientIp(ServerHttpRequest request) {
        String xForwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddress() != null 
            ? request.getRemoteAddress().getAddress().getHostAddress() 
            : "unknown";
    }

    @Override
    public int getOrder() {
        return -1; // Execute before other filters
    }
}
