package com.banking.core.infrastructure.audit;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record EventPayload(
    UUID eventId,
    UUID sourceAccountId,
    UUID targetAccountId,
    String eventKind,
    String eventStatus,
    BigDecimal amount,
    String currency,
    String eventKey,
    Instant occurredAt
) {
}
