package com.banking.account.domain.exception;

import java.util.UUID;

public class AccountClosedException extends RuntimeException {
    public AccountClosedException(UUID accountId) {
        super("Account is closed: " + accountId);
    }
}
