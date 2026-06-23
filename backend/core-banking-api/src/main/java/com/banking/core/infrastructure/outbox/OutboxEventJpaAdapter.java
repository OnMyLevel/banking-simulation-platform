package com.banking.core.infrastructure.outbox;

import com.banking.core.domain.model.OutboxEvent;
import com.banking.core.domain.model.OutboxEventStatus;
import com.banking.core.domain.repository.OutboxEventRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public class OutboxEventJpaAdapter implements OutboxEventRepository {
    private final OutboxEventJpaRepository jpaRepository;

    public OutboxEventJpaAdapter(OutboxEventJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public OutboxEvent persist(OutboxEvent event) {
        return OutboxEventMapper.toDomain(jpaRepository.save(OutboxEventMapper.toEntity(event)));
    }

    @Override
    public List<OutboxEvent> findPendingEvents(Instant now, int limit) {
        return jpaRepository.findByStatusInAndNextRetryAtLessThanEqualOrderByCreatedAtAsc(
                List.of(OutboxEventStatus.PENDING, OutboxEventStatus.FAILED),
                now,
                PageRequest.of(0, limit)
            )
            .stream()
            .map(OutboxEventMapper::toDomain)
            .toList();
    }
}
