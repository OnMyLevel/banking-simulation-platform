package com.banking.observability.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ReceivedEvent(
    UUID id,
    UUID eventId,
    UUID sourceAccountId,
    UUID targetAccountId,
    String eventKind,
    String eventStatus,
    BigDecimal amount,
    String currency,
    String eventKey,
    Instant occurredAt,
    Instant receivedAt
) {
    public static ReceivedEvent receive(UUID eventId, UUID sourceAccountId, UUID targetAccountId, String eventKind, String eventStatus, BigDecimal amount, String currency, String eventKey, Instant occurredAt) {
        return new ReceivedEvent(
            UUID.randomUUID(),
            eventId,
            sourceAccountId,
            targetAccountId,
            eventKind,
            eventStatus,
            amount,
            currency,
            eventKey,
            occurredAt,
            Instant.now()
        );
    }
}
