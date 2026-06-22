package com.banking.core.domain.exception;

import java.util.UUID;

public class CoreAccountUnavailableException extends RuntimeException {
    public CoreAccountUnavailableException(UUID accountId) {
        super("Unavailable account: " + accountId);
    }
}
