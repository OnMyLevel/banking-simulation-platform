package com.banking.core.domain.exception;

public class AccountServiceUnavailableException extends RuntimeException {
    public AccountServiceUnavailableException(Throwable cause) {
        super("Account service unavailable", cause);
    }
}
