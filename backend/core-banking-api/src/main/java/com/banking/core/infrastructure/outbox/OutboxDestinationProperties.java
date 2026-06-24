package com.banking.core.infrastructure.outbox;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "banking.outbox")
public record OutboxDestinationProperties(
    String destinationType
) {
    public String safeDestinationType() {
        return destinationType == null || destinationType.isBlank() ? "OBSERVABILITY_HTTP" : destinationType;
    }
}
