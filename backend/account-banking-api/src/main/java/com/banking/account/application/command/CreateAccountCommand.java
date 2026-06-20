package com.banking.account.application.command;

import java.util.UUID;

public record CreateAccountCommand(
    UUID ownerId,
    String currency
) {}
