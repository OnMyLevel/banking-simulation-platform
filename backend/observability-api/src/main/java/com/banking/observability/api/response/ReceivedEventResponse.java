package com.banking.observability.api.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ReceivedEventResponse(
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
) {}
