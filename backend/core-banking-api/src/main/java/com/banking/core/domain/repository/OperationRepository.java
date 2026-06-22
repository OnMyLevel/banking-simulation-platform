package com.banking.core.domain.repository;

import com.banking.core.domain.model.Operation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OperationRepository {
    Operation persist(Operation operation);
    Optional<Operation> findByIdempotencyKey(String idempotencyKey);
    List<Operation> findByAccountId(UUID accountId, int limit, int offset);
}
