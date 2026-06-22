package com.banking.core.api.response;

import java.time.Instant;
import java.util.UUID;

public record OperationResponse(
    UUID id,
    UUID sourceAccountId,
    UUID targetAccountId,
    String kind,
    String status,
    String amount,
    String currency,
    Instant createdAt
) {
}
