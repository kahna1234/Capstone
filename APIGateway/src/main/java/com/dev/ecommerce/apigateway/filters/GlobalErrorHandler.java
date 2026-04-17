package com.dev.ecommerce.apigateway.filters;

import jakarta.annotation.Nonnull;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Order(-2)
public class GlobalErrorHandler implements WebExceptionHandler {
    @Override
    public @Nonnull Mono<Void> handle(@Nonnull ServerWebExchange exchange, @Nonnull Throwable ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Internal Server Error";
        if (ex instanceof NotFoundException) {
            status = HttpStatus.NOT_FOUND;
            message = "Service Not Found";
        } else if (ex instanceof ResponseStatusException) {
            int rawStatus = ((ResponseStatusException) ex).getStatusCode().value();
            HttpStatus resolvedStatus = HttpStatus.resolve(rawStatus);
            status = resolvedStatus != null ? resolvedStatus : HttpStatus.INTERNAL_SERVER_ERROR;
            message = ex.getMessage();
        }
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] bytes = ("{\"error\":\"" + message + "\"}").getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
    }
}
