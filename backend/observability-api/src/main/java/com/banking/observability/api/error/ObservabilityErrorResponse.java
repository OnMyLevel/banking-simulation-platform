package com.banking.observability.api.error;

import java.time.Instant;

public record ObservabilityErrorResponse(
    String code,
    String message,
    Instant timestamp
) {
    public static ObservabilityErrorResponse of(String code, String message) {
        return new ObservabilityErrorResponse(code, message, Instant.now());
    }
}
