package com.banking.core.infrastructure.persistence.adapter;

import com.banking.core.domain.model.Operation;
import com.banking.core.domain.repository.OperationRepository;
import com.banking.core.infrastructure.persistence.jpa.OperationJpaRepository;
import com.banking.core.infrastructure.persistence.mapper.OperationMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OperationJpaAdapter implements OperationRepository {
    private final OperationJpaRepository operationJpaRepository;

    public OperationJpaAdapter(OperationJpaRepository operationJpaRepository) {
        this.operationJpaRepository = operationJpaRepository;
    }

    @Override
    public Operation persist(Operation operation) {
        return OperationMapper.toDomain(operationJpaRepository.save(OperationMapper.toEntity(operation)));
    }

    @Override
    public Optional<Operation> findByIdempotencyKey(String idempotencyKey) {
        return operationJpaRepository.findByIdempotencyKey(idempotencyKey).map(OperationMapper::toDomain);
    }

    @Override
    public List<Operation> findByAccountId(UUID accountId, int limit, int offset) {
        int page = offset / limit;
        return operationJpaRepository.findByAccountId(accountId, PageRequest.of(page, limit))
            .stream()
            .map(OperationMapper::toDomain)
            .toList();
    }
}
