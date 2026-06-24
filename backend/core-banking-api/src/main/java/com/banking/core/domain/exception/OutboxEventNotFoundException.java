package com.banking.core.domain.exception;

import java.util.UUID;

public class OutboxEventNotFoundException extends RuntimeException {
    public OutboxEventNotFoundException(UUID eventId) {
        super("Outbox event not found: " + eventId);
    }
}
