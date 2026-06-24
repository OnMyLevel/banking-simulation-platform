package com.banking.core.infrastructure.outbox;

import com.banking.core.domain.model.OutboxEvent;

import java.time.Instant;

public class OutboxRetryPolicy {
    private final OutboxRetryProperties properties;

    public OutboxRetryPolicy(OutboxRetryProperties properties) {
        this.properties = properties;
    }

    public OutboxEvent failed(OutboxEvent event, RuntimeException exception, Instant now) {
        if (event.retryCount() + 1 >= properties.safeMaxRetryCount()) {
            return event.failed(errorMessage(exception), Instant.MAX);
        }
        return event.failed(errorMessage(exception), now.plus(properties.safeRetryDelay()));
    }

    public int batchSize() {
        return properties.safeBatchSize();
    }

    private String errorMessage(RuntimeException exception) {
        return exception.getMessage() == null ? exception.getClass().getSimpleName() : exception.getMessage();
    }
}
