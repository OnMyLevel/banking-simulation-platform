package com.banking.core.infrastructure.audit;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "banking.observability-api")
public record AuditClientProperties(
    String baseUrl,
    Duration connectTimeout,
    Duration readTimeout
) {
}
