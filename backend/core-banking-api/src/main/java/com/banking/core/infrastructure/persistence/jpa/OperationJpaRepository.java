package com.banking.core.infrastructure.persistence.jpa;

import com.banking.core.infrastructure.persistence.entity.OperationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OperationJpaRepository extends JpaRepository<OperationEntity, UUID> {
    Optional<OperationEntity> findByIdempotencyKey(String idempotencyKey);

    @Query("select o from OperationEntity o where o.sourceAccountId = :accountId or o.targetAccountId = :accountId order by o.createdAt desc")
    List<OperationEntity> findByAccountId(UUID accountId, Pageable pageable);

    @Query("select o from OperationEntity o where o.sourceAccountId = :accountId or o.targetAccountId = :accountId")
    List<OperationEntity> findAllByAccountId(UUID accountId);
}
