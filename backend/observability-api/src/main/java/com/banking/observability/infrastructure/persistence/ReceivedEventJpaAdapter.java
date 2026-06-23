package com.banking.observability.infrastructure.persistence;

import com.banking.observability.domain.model.ReceivedEvent;
import com.banking.observability.domain.repository.ReceivedEventRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
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
    public Optional<ReceivedEvent> findByEventId(UUID eventId) {
        return jpaRepository.findByEventId(eventId).map(ReceivedEventMapper::toDomain);
    }

    @Override
    public boolean existsByEventId(UUID eventId) {
        return jpaRepository.existsByEventId(eventId);
    }
}
