package com.banking.user.api.error;

import com.banking.user.domain.exception.EmailAlreadyUsedException;
import com.banking.user.domain.exception.UserDomainException;
import com.banking.user.domain.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        return new ApiErrorResponse("VALIDATION_ERROR", "Invalid request payload", Instant.now(), "not-provided-yet");
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleNotFound(UserNotFoundException ex) {
        return toErrorResponse(ex);
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleConflict(EmailAlreadyUsedException ex) {
        return toErrorResponse(ex);
    }

    @ExceptionHandler(UserDomainException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiErrorResponse handleDomain(UserDomainException ex) {
        return toErrorResponse(ex);
    }

    private ApiErrorResponse toErrorResponse(UserDomainException ex) {
        return new ApiErrorResponse(ex.code(), ex.getMessage(), Instant.now(), "not-provided-yet");
    }
}
