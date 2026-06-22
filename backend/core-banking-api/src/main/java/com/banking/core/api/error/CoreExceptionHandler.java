package com.banking.core.api.error;

import com.banking.core.domain.exception.IdempotencyKeyRequiredException;
import com.banking.core.domain.exception.InsufficientFundsException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CoreErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        return error("VALIDATION_ERROR", "Invalid request payload");
    }

    private CoreErrorResponse error(String code, String message) {
        return new CoreErrorResponse(code, message, Instant.now(), "not-provided-yet");
    }
}
