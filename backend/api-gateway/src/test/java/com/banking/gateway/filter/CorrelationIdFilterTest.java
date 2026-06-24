package com.banking.gateway.filter;

import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class CorrelationIdFilterTest {
    private final CorrelationIdFilter filter = new CorrelationIdFilter();

    @Test
    void shouldKeepExistingCorrelationId() {
        AtomicReference<ServerWebExchange> exchangeRef = new AtomicReference<>();
        MockServerWebExchange exchange = MockServerWebExchange.from(
            MockServerHttpRequest.get("/api/accounts/accounts/123")
                .header(CorrelationIdFilter.CORRELATION_ID_HEADER, "correlation-123")
        );
        GatewayFilterChain chain = currentExchange -> {
            exchangeRef.set(currentExchange);
            return Mono.empty();
        };

        filter.filter(exchange, chain).block();

        assertThat(exchangeRef.get().getRequest().getHeaders().getFirst(CorrelationIdFilter.CORRELATION_ID_HEADER))
            .isEqualTo("correlation-123");
    }

    @Test
    void shouldGenerateCorrelationIdWhenMissing() {
        AtomicReference<ServerWebExchange> exchangeRef = new AtomicReference<>();
        MockServerWebExchange exchange = MockServerWebExchange.from(
            MockServerHttpRequest.get("/api/operations/operations/credits")
        );
        GatewayFilterChain chain = currentExchange -> {
            exchangeRef.set(currentExchange);
            return Mono.empty();
        };

        filter.filter(exchange, chain).block();

        assertThat(exchangeRef.get().getRequest().getHeaders().getFirst(CorrelationIdFilter.CORRELATION_ID_HEADER))
            .isNotBlank();
    }
}
