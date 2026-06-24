package com.banking.gateway.logging;

import com.banking.gateway.filter.CorrelationIdFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;

@Component
public class GatewayRequestLoggingFilter implements GlobalFilter, Ordered {
    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayRequestLoggingFilter.class);
    private static final int FILTER_ORDER = -90;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Instant startedAt = Instant.now();
        String method = exchange.getRequest().getMethod().name();
        String path = exchange.getRequest().getPath().pathWithinApplication().value();
        String correlationId = exchange.getRequest().getHeaders().getFirst(CorrelationIdFilter.CORRELATION_ID_HEADER);

        return chain.filter(exchange)
            .doOnSuccess(ignored -> logExchange(exchange, method, path, correlationId, startedAt))
            .doOnError(error -> logExchangeError(method, path, correlationId, startedAt, error));
    }

    @Override
    public int getOrder() {
        return FILTER_ORDER;
    }

    private void logExchange(ServerWebExchange exchange, String method, String path, String correlationId, Instant startedAt) {
        HttpStatusCode statusCode = exchange.getResponse().getStatusCode();
        int status = statusCode == null ? 200 : statusCode.value();
        long durationMs = Duration.between(startedAt, Instant.now()).toMillis();
        LOGGER.info("gateway_request method={} path={} status={} durationMs={} correlationId={}", method, path, status, durationMs, safe(correlationId));
    }

    private void logExchangeError(String method, String path, String correlationId, Instant startedAt, Throwable error) {
        long durationMs = Duration.between(startedAt, Instant.now()).toMillis();
        LOGGER.warn("gateway_request_error method={} path={} durationMs={} correlationId={} error={}", method, path, durationMs, safe(correlationId), error.getClass().getSimpleName());
    }

    private String safe(String value) {
        if (value == null || value.isBlank()) {
            return "missing";
        }
        return value;
    }
}
