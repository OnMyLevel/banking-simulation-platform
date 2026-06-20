package com.banking.user.api.error;

import java.time.Instant;

public record ApiErrorResponse(
    String code,
    String message,
    Instant timestamp,
    String correlationId
) {
}
