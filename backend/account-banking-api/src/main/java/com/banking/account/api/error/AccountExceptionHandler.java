package com.banking.account.api.error;

import com.banking.account.domain.exception.AccountNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class AccountExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AccountErrorResponse handleAccountNotFound(AccountNotFoundException ex) {
        return new AccountErrorResponse(
            "ACCOUNT_NOT_FOUND",
            ex.getMessage(),
            Instant.now(),
            "not-provided-yet"
        );
    }
}
