package com.dev.ecommerce.apigateway.filters;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

@Component
@Order(-1)
public class GlobalErrorHandler implements WebExceptionHandler {

    private static final List<String> SAFE_CORS_ORIGIN_SUFFIXES = List.of(
            ".ngrok-free.dev"
    );

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        // ✅ Default status
        HttpStatusCode status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Internal Server Error";

        // ✅ Handle known exceptions
        if (ex instanceof ResponseStatusException rse) {
            status = rse.getStatusCode();
            message = rse.getReason() != null ? rse.getReason() : message;
        }

        // ✅ Build JSON response
        String responseBody = String.format("""
                {
                  "timestamp": "%s",
                  "status": %d,
                  "error": "%s",
                  "path": "%s"
                }
                """,
                Instant.now(),
                status.value(),
                message,
                exchange.getRequest().getPath()
        );

        // ✅ Set response
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        addCorsHeadersIfApplicable(exchange);

        byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);

        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(bytes)));
    }

    private void addCorsHeadersIfApplicable(ServerWebExchange exchange) {
        String origin = exchange.getRequest().getHeaders().getOrigin();
        if (origin == null || origin.isBlank()) {
            return;
        }

        boolean allow =
                origin.startsWith("http://localhost:") ||
                origin.startsWith("https://localhost:") ||
                SAFE_CORS_ORIGIN_SUFFIXES.stream().anyMatch(origin::endsWith);

        if (!allow) {
            return;
        }

        exchange.getResponse().getHeaders().set("Access-Control-Allow-Origin", origin);
        exchange.getResponse().getHeaders().set("Vary", "Origin");
        exchange.getResponse().getHeaders().set("Access-Control-Allow-Credentials", "true");
        exchange.getResponse().getHeaders().set(
                "Access-Control-Allow-Headers",
                "Authorization, Content-Type, X-User-Id, X-User-Roles"
        );
        exchange.getResponse().getHeaders().set(
                "Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS"
        );
    }
}