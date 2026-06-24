package com.banking.core.api.response;

import com.banking.core.domain.model.OutboxEventStatus;

import java.time.Instant;
import java.util.UUID;

public record OutboxEventResponse(
    UUID id,
    UUID aggregateId,
    String eventType,
    String destinationType,
    OutboxEventStatus status,
    int retryCount,
    String lastError,
    Instant nextRetryAt,
    Instant createdAt,
    Instant sentAt
) {
}
