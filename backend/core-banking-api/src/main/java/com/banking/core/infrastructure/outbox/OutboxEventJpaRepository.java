package com.banking.core.infrastructure.outbox;

import com.banking.core.domain.model.OutboxEventStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface OutboxEventJpaRepository extends JpaRepository<OutboxEventEntity, UUID> {
    List<OutboxEventEntity> findByStatusInAndNextRetryAtLessThanEqualOrderByCreatedAtAsc(List<OutboxEventStatus> statuses, Instant now, Pageable pageable);
    List<OutboxEventEntity> findByStatusOrderByCreatedAtDesc(OutboxEventStatus status, Pageable pageable);
    long countByStatus(OutboxEventStatus status);
}
