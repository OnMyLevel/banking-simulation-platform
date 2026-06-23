package com.banking.observability.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReceivedEventJpaRepository extends JpaRepository<ReceivedEventEntity, UUID> {
    Optional<ReceivedEventEntity> findByEventId(UUID eventId);
    boolean existsByEventId(UUID eventId);
}
