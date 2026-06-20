package com.banking.account.api.response;

import java.time.Instant;
import java.util.UUID;

public record AccountResponse(
    UUID id,
    UUID ownerId,
    String iban,
    String status,
    MoneyResponse balance,
    Instant createdAt,
    Instant updatedAt
) {
}
