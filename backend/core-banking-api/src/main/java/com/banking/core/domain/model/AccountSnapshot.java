package com.banking.core.domain.model;

import java.util.UUID;

public record AccountSnapshot(
    UUID id,
    String status
) {
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }
}
