package com.banking.core.api.error;

import java.time.Instant;

public record CoreErrorResponse(
    String code,
    String message,
    Instant timestamp,
    String correlationId
) {
}
