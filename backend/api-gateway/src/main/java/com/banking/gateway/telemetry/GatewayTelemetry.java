package com.banking.gateway.telemetry;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class GatewayTelemetry {
    private static final String REQUEST_COUNTER = "banking.gateway.requests";
    private static final String DURATION_TIMER = "banking.gateway.request.duration";
    private static final String TRAFFIC_REJECTION_COUNTER = "banking.gateway.traffic.rejections";
    private final MeterRegistry meterRegistry;

    public GatewayTelemetry(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void recordRequest(String method, String path, HttpStatusCode statusCode, Duration duration) {
        String routeFamily = routeFamily(path);
        String status = status(statusCode);
        String statusFamily = statusFamily(statusCode);

        Counter.builder(REQUEST_COUNTER)
            .tag("route", routeFamily)
            .tag("method", method)
            .tag("status", status)
            .tag("statusFamily", statusFamily)
            .register(meterRegistry)
            .increment();

        Timer.builder(DURATION_TIMER)
            .tag("route", routeFamily)
            .tag("method", method)
            .tag("statusFamily", statusFamily)
            .register(meterRegistry)
            .record(duration);
    }

    public void recordTrafficRejection(String path) {
        Counter.builder(TRAFFIC_REJECTION_COUNTER)
            .tag("route", routeFamily(path))
            .register(meterRegistry)
            .increment();
    }

    String routeFamily(String path) {
        if (path == null || path.isBlank()) {
            return "unknown";
        }
        if (path.startsWith("/api/users/")) {
            return "users";
        }
        if (path.startsWith("/api/accounts/")) {
            return "accounts";
        }
        if (path.startsWith("/api/operations/")) {
            return "operations";
        }
        if (path.startsWith("/actuator/")) {
            return "actuator";
        }
        if (path.startsWith("/internal/")) {
            return "internal";
        }
        return "default";
    }

    private String status(HttpStatusCode statusCode) {
        if (statusCode == null) {
            return "200";
        }
        return String.valueOf(statusCode.value());
    }

    private String statusFamily(HttpStatusCode statusCode) {
        int value = statusCode == null ? 200 : statusCode.value();
        if (value >= 200 && value < 300) {
            return "2xx";
        }
        if (value >= 300 && value < 400) {
            return "3xx";
        }
        if (value >= 400 && value < 500) {
            return "4xx";
        }
        if (value >= 500) {
            return "5xx";
        }
        return "other";
    }
}
