package com.banking.core.infrastructure.account;

import java.util.UUID;

public record AccountHttpResponse(
    UUID id,
    String status
) {
}
