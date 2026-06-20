package com.banking.account.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateAccountRequest(
    @NotNull UUID ownerId,
    @NotBlank @Size(min = 3, max = 3) String currency
) {}
