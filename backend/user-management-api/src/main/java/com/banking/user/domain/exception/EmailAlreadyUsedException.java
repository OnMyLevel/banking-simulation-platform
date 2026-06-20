package com.banking.user.domain.exception;

public class EmailAlreadyUsedException extends UserDomainException {
    public EmailAlreadyUsedException(String email) {
        super("EMAIL_ALREADY_USED", "Email already used: " + email);
    }
}
