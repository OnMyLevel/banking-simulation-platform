package com.banking.core.api.mapper;

import com.banking.core.api.response.OutboxEventResponse;
import com.banking.core.domain.model.OutboxEvent;

public final class OutboxEventApiMapper {
    private OutboxEventApiMapper() {
    }

    public static OutboxEventResponse toResponse(OutboxEvent event) {
        return new OutboxEventResponse(
            event.id(),
            event.aggregateId(),
            event.eventType(),
            event.destinationType(),
            event.status(),
            event.retryCount(),
            event.lastError(),
            event.nextRetryAt(),
            event.createdAt(),
            event.sentAt()
        );
    }
}
