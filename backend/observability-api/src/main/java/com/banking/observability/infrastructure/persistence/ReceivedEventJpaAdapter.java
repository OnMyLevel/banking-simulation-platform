package com.banking.observability.infrastructure.persistence;

import com.banking.observability.domain.model.ReceivedEvent;
import com.banking.observability.domain.repository.ReceivedEventRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class ReceivedEventJpaAdapter implements ReceivedEventRepository {
    private final ReceivedEventJpaRepository jpaRepository;

    public ReceivedEventJpaAdapter(ReceivedEventJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ReceivedEvent persist(ReceivedEvent event) {
        return ReceivedEventMapper.toDomain(jpaRepository.save(ReceivedEventMapper.toEntity(event)));
    }

    @Override
    public boolean existsByEventId(UUID eventId) {
        return jpaRepository.existsByEventId(eventId);
    }
}
