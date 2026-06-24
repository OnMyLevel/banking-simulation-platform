package com.banking.core.infrastructure.outbox;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "banking.outbox")
public record OutboxRetryProperties(
    Duration retryDelay,
    int maxRetryCount,
    int batchSize
) {
    public Duration safeRetryDelay() {
        return retryDelay == null ? Duration.ofMinutes(1) : retryDelay;
    }

    public int safeMaxRetryCount() {
        return maxRetryCount <= 0 ? 5 : maxRetryCount;
    }

    public int safeBatchSize() {
        return batchSize <= 0 ? 25 : batchSize;
    }
}
