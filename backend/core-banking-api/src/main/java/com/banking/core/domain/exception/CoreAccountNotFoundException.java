package com.banking.core.domain.exception;

import java.util.UUID;

public class CoreAccountNotFoundException extends RuntimeException {
    public CoreAccountNotFoundException(UUID accountId) {
        super("Account not found: " + accountId);
    }
}
