package com.banking.core.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Operation {
    private final UUID id;
    private final UUID sourceAccountId;
    private final UUID targetAccountId;
    private final OperationKind kind;
    private final OperationStatus status;
    private final Money money;
    private final String idempotencyKey;
    private final Instant createdAt;

    public Operation(UUID id, UUID sourceAccountId, UUID targetAccountId, OperationKind kind, OperationStatus status, Money money, String idempotencyKey, Instant createdAt) {
        this.id = id;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.kind = kind;
        this.status = status;
        this.money = money;
        this.idempotencyKey = idempotencyKey;
        this.createdAt = createdAt;
    }

    public static Operation credit(UUID accountId, Money money, String idempotencyKey) {
        return new Operation(UUID.randomUUID(), null, accountId, OperationKind.CREDIT, OperationStatus.COMPLETED, money, idempotencyKey, Instant.now());
    }

    public static Operation debit(UUID accountId, Money money, String idempotencyKey) {
        return new Operation(UUID.randomUUID(), accountId, null, OperationKind.DEBIT, OperationStatus.COMPLETED, money, idempotencyKey, Instant.now());
    }

    public static Operation transfer(UUID sourceAccountId, UUID targetAccountId, Money money, String idempotencyKey) {
        return new Operation(UUID.randomUUID(), sourceAccountId, targetAccountId, OperationKind.TRANSFER, OperationStatus.COMPLETED, money, idempotencyKey, Instant.now());
    }

    public UUID id() { return id; }
    public UUID sourceAccountId() { return sourceAccountId; }
    public UUID targetAccountId() { return targetAccountId; }
    public OperationKind kind() { return kind; }
    public OperationStatus status() { return status; }
    public Money money() { return money; }
    public String idempotencyKey() { return idempotencyKey; }
    public Instant createdAt() { return createdAt; }
}
