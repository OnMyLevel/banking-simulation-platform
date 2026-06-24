package com.banking.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {
    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    private static final int FILTER_ORDER = -100;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String correlationId = resolveCorrelationId(exchange.getRequest().getHeaders());

        ServerHttpRequest request = exchange.getRequest()
            .mutate()
            .headers(headers -> headers.set(CORRELATION_ID_HEADER, correlationId))
            .build();

        ServerWebExchange mutatedExchange = exchange.mutate().request(request).build();
        mutatedExchange.getResponse().beforeCommit(() -> {
            mutatedExchange.getResponse().getHeaders().set(CORRELATION_ID_HEADER, correlationId);
            return Mono.empty();
        });

        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return FILTER_ORDER;
    }

    private String resolveCorrelationId(HttpHeaders headers) {
        String existingCorrelationId = headers.getFirst(CORRELATION_ID_HEADER);
        if (existingCorrelationId == null || existingCorrelationId.isBlank()) {
            return UUID.randomUUID().toString();
        }
        return existingCorrelationId;
    }
}
