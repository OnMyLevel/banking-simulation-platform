package com.banking.observability.api.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ReceiveEventRequest(
    @NotNull UUID eventId,
    UUID sourceAccountId,
    UUID targetAccountId,
    @NotBlank @Size(max = 32) String eventKind,
    @NotBlank @Size(max = 32) String eventStatus,
    @NotNull @DecimalMin(value = "0.01") BigDecimal amount,
    @NotBlank @Size(min = 3, max = 3) String currency,
    @NotBlank @Size(max = 128) String eventKey,
    @NotNull Instant occurredAt
) {}
