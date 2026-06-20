package com.banking.user.api.response;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String firstname,
    String lastname,
    String email,
    String status,
    Set<String> roles,
    Instant createdAt,
    Instant updatedAt
) {
}
