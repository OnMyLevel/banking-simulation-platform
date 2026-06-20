package com.banking.user.domain.exception;

import java.util.UUID;

public class UserNotFoundException extends UserDomainException {
    public UserNotFoundException(UUID userId) {
        super("USER_NOT_FOUND", "User not found: " + userId);
    }
}
