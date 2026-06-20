package com.banking.account.api.error;

import com.banking.account.domain.exception.AccountClosedException;
import com.banking.account.domain.exception.AccountNotActiveException;
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
        return error("ACCOUNT_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(AccountNotActiveException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public AccountErrorResponse handleAccountNotActive(AccountNotActiveException ex) {
        return error("ACCOUNT_NOT_ACTIVE", ex.getMessage());
    }

    @ExceptionHandler(AccountClosedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public AccountErrorResponse handleAccountClosed(AccountClosedException ex) {
        return error("ACCOUNT_CLOSED", ex.getMessage());
    }

    private AccountErrorResponse error(String code, String message) {
        return new AccountErrorResponse(code, message, Instant.now(), "not-provided-yet");
    }
}
