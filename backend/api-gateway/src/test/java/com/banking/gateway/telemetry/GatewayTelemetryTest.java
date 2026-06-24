package com.banking.gateway.telemetry;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class GatewayTelemetryTest {
    @Test
    void shouldRecordRequestCounterAndDuration() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        GatewayTelemetry telemetry = new GatewayTelemetry(meterRegistry);

        telemetry.recordRequest("GET", "/api/accounts/accounts/123", HttpStatus.OK, Duration.ofMillis(42));

        assertThat(meterRegistry.find("banking.gateway.requests")
            .tag("route", "accounts")
            .tag("method", "GET")
            .tag("status", "200")
            .tag("statusFamily", "2xx")
            .counter()
            .count()).isEqualTo(1.0);
        assertThat(meterRegistry.find("banking.gateway.request.duration")
            .tag("route", "accounts")
            .tag("method", "GET")
            .tag("statusFamily", "2xx")
            .timer()
            .count()).isEqualTo(1L);
    }

    @Test
    void shouldRecordTrafficRejection() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        GatewayTelemetry telemetry = new GatewayTelemetry(meterRegistry);

        telemetry.recordTrafficRejection("/api/operations/operations/credits");

        assertThat(meterRegistry.find("banking.gateway.traffic.rejections")
            .tag("route", "operations")
            .counter()
            .count()).isEqualTo(1.0);
    }

    @Test
    void shouldResolveRouteFamily() {
        GatewayTelemetry telemetry = new GatewayTelemetry(new SimpleMeterRegistry());

        assertThat(telemetry.routeFamily("/api/users/me")).isEqualTo("users");
        assertThat(telemetry.routeFamily("/api/accounts/accounts/1")).isEqualTo("accounts");
        assertThat(telemetry.routeFamily("/api/operations/operations/credits")).isEqualTo("operations");
        assertThat(telemetry.routeFamily("/actuator/health")).isEqualTo("actuator");
        assertThat(telemetry.routeFamily("/internal/outbox-events")).isEqualTo("internal");
    }
}
