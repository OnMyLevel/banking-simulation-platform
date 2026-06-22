package com.banking.core.api.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TransferRequest(
    @NotNull UUID sourceAccountId,
    @NotNull UUID targetAccountId,
    @Valid @NotNull MoneyRequest money
) {
}
