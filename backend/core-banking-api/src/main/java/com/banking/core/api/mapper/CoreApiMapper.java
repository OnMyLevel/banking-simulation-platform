package com.banking.core.api.mapper;

import com.banking.core.api.request.MoneyRequest;
import com.banking.core.api.response.OperationResponse;
import com.banking.core.domain.model.Money;
import com.banking.core.domain.model.Operation;

public final class CoreApiMapper {
    private CoreApiMapper() {
    }

    public static Money toMoney(MoneyRequest request) {
        return Money.of(request.amount(), request.currency());
    }

    public static OperationResponse toResponse(Operation operation) {
        return new OperationResponse(
            operation.id(),
            operation.sourceAccountId(),
            operation.targetAccountId(),
            operation.kind().name(),
            operation.status().name(),
            operation.money().amount().toPlainString(),
            operation.money().currency(),
            operation.createdAt()
        );
    }
}
