package com.banking.core.api.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreditRequest(
    @NotNull UUID accountId,
    @Valid @NotNull MoneyRequest money
) {
}
