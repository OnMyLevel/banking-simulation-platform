package com.banking.core.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record AuditEvent(
    UUID operationId,
    UUID sourceAccountId,
    UUID targetAccountId,
    OperationKind operationKind,
    OperationStatus operationStatus,
    BigDecimal amount,
    String currency,
    String idempotencyKey,
    Instant occurredAt
) {
    public static AuditEvent from(Operation operation) {
        return new AuditEvent(
            operation.id(),
            operation.sourceAccountId(),
            operation.targetAccountId(),
            operation.kind(),
            operation.status(),
            operation.money().amount(),
            operation.money().currency(),
            operation.idempotencyKey(),
            operation.createdAt()
        );
    }
}
