package com.banking.gateway.logging;

import com.banking.gateway.filter.CorrelationIdFilter;
import com.banking.gateway.telemetry.GatewayTelemetry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

class GatewayRequestLoggingFilterTest {
    private final SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
    private final GatewayRequestLoggingFilter filter = new GatewayRequestLoggingFilter(new GatewayTelemetry(meterRegistry));

    @Test
    void shouldContinueFilterChain() {
        AtomicBoolean called = new AtomicBoolean(false);
        ServerWebExchange exchange = MockServerWebExchange.from(
            MockServerHttpRequest.get("/api/accounts/accounts/123")
                .header(CorrelationIdFilter.CORRELATION_ID_HEADER, "correlation-123")
        );
        GatewayFilterChain chain = currentExchange -> {
            called.set(true);
            return Mono.empty();
        };

        filter.filter(exchange, chain).block();

        assertThat(called).isTrue();
        assertThat(meterRegistry.find("banking.gateway.requests").counter().count()).isEqualTo(1.0);
    }

    @Test
    void shouldPropagateChainError() {
        RuntimeException expected = new RuntimeException("boom");
        ServerWebExchange exchange = MockServerWebExchange.from(
            MockServerHttpRequest.get("/api/operations/operations/credits")
                .header(CorrelationIdFilter.CORRELATION_ID_HEADER, "correlation-456")
        );
        GatewayFilterChain chain = currentExchange -> Mono.error(expected);

        Throwable result = org.assertj.core.api.Assertions.catchThrowable(() -> filter.filter(exchange, chain).block());

        assertThat(result).isSameAs(expected);
    }
}
