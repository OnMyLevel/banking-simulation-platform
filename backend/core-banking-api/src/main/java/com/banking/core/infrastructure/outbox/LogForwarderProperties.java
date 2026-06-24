package com.banking.core.infrastructure.outbox;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "banking.log-forwarder")
public record LogForwarderProperties(
    String baseUrl,
    Duration connectTimeout,
    Duration readTimeout
) {
    public String safeBaseUrl() {
        return baseUrl == null || baseUrl.isBlank() ? "http://localhost:24224" : baseUrl;
    }

    public Duration safeConnectTimeout() {
        return connectTimeout == null ? Duration.ofSeconds(1) : connectTimeout;
    }

    public Duration safeReadTimeout() {
        return readTimeout == null ? Duration.ofSeconds(2) : readTimeout;
    }
}
