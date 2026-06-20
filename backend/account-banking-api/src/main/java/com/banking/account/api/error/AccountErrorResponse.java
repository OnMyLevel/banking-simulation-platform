package com.banking.account.api.error;

import java.time.Instant;

public record AccountErrorResponse(
    String code,
    String message,
    Instant timestamp,
    String correlationId
) {
}
