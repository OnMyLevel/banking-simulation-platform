package com.banking.core.api.error;

import com.banking.core.domain.exception.AccountServiceUnavailableException;
import com.banking.core.domain.exception.CoreAccountNotFoundException;
import com.banking.core.domain.exception.CoreAccountUnavailableException;
import com.banking.core.domain.exception.IdempotencyKeyRequiredException;
import com.banking.core.domain.exception.InsufficientFundsException;
import com.banking.core.domain.exception.OutboxEventNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class CoreExceptionHandler {

    @ExceptionHandler(IdempotencyKeyRequiredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CoreErrorResponse handleMissingIdempotencyKey(IdempotencyKeyRequiredException ex) {
        return error("IDEMPOTENCY_KEY_REQUIRED", ex.getMessage());
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public CoreErrorResponse handleBalanceError(InsufficientFundsException ex) {
        return error("INSUFFICIENT_FUNDS", ex.getMessage());
    }

    @ExceptionHandler(CoreAccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CoreErrorResponse handleAccountNotFound(CoreAccountNotFoundException ex) {
        return error("ACCOUNT_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(CoreAccountUnavailableException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public CoreErrorResponse handleAccountUnavailable(CoreAccountUnavailableException ex) {
        return error("ACCOUNT_NOT_ACTIVE", ex.getMessage());
    }

    @ExceptionHandler(AccountServiceUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public CoreErrorResponse handleAccountServiceUnavailable(AccountServiceUnavailableException ex) {
        return error("ACCOUNT_SERVICE_UNAVAILABLE", ex.getMessage());
    }

    @ExceptionHandler(OutboxEventNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CoreErrorResponse handleOutboxEventNotFound(OutboxEventNotFoundException ex) {
        return error("OUTBOX_EVENT_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CoreErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        return error("VALIDATION_ERROR", "Invalid request payload");
    }

    private CoreErrorResponse error(String code, String message) {
        return new CoreErrorResponse(code, message, Instant.now(), "not-provided-yet");
    }
}
