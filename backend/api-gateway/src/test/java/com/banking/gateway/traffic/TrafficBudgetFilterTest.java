package com.banking.gateway.traffic;

import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class TrafficBudgetFilterTest {
    @Test
    void shouldAllowRequestsWithinBudget() {
        GatewayTrafficProperties properties = new GatewayTrafficProperties();
        properties.setOperationsBudgetPerMinute(2);
        TrafficBudgetFilter filter = new TrafficBudgetFilter(properties, localStore());
        AtomicInteger calls = new AtomicInteger();
        WebFilterChain chain = exchange -> {
            calls.incrementAndGet();
            return Mono.empty();
        };

        filter.filter(exchange("/api/operations/operations/credits"), chain).block();
        filter.filter(exchange("/api/operations/operations/credits"), chain).block();

        assertThat(calls).hasValue(2);
    }

    @Test
    void shouldRejectRequestsAboveBudget() {
        GatewayTrafficProperties properties = new GatewayTrafficProperties();
        properties.setOperationsBudgetPerMinute(1);
        TrafficBudgetFilter filter = new TrafficBudgetFilter(properties, localStore());
        WebFilterChain chain = exchange -> Mono.empty();

        MockServerWebExchange first = exchange("/api/operations/operations/credits");
        MockServerWebExchange second = exchange("/api/operations/operations/credits");

        filter.filter(first, chain).block();
        filter.filter(second, chain).block();

        assertThat(second.getResponse().getStatusCode().value()).isEqualTo(429);
        assertThat(second.getResponse().getHeaders().getFirst("Retry-After")).isEqualTo("60");
    }

    private MockServerWebExchange exchange(String path) {
        return MockServerWebExchange.from(
            MockServerHttpRequest.get(path)
                .remoteAddress(new java.net.InetSocketAddress("127.0.0.1", 8080))
        );
    }

    private LocalTrafficBudgetStore localStore() {
        return new LocalTrafficBudgetStore(fixedClock());
    }

    private Clock fixedClock() {
        return Clock.fixed(Instant.parse("2026-06-24T14:00:00Z"), ZoneOffset.UTC);
    }
}
