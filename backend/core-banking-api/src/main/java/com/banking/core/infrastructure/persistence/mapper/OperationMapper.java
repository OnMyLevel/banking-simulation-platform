package com.banking.core.infrastructure.persistence.mapper;

import com.banking.core.domain.model.Money;
import com.banking.core.domain.model.Operation;
import com.banking.core.domain.model.OperationKind;
import com.banking.core.domain.model.OperationStatus;
import com.banking.core.infrastructure.persistence.entity.OperationEntity;

public final class OperationMapper {
    private OperationMapper() {
    }

    public static OperationEntity toEntity(Operation operation) {
        OperationEntity entity = new OperationEntity();
        entity.id = operation.id();
        entity.sourceAccountId = operation.sourceAccountId();
        entity.targetAccountId = operation.targetAccountId();
        entity.kind = operation.kind().name();
        entity.status = operation.status().name();
        entity.amount = operation.money().amount();
        entity.currency = operation.money().currency();
        entity.idempotencyKey = operation.idempotencyKey();
        entity.createdAt = operation.createdAt();
        return entity;
    }

    public static Operation toDomain(OperationEntity entity) {
        return new Operation(
            entity.id,
            entity.sourceAccountId,
            entity.targetAccountId,
            OperationKind.valueOf(entity.kind),
            OperationStatus.valueOf(entity.status),
            new Money(entity.amount, entity.currency),
            entity.idempotencyKey,
            entity.createdAt
        );
    }
}
