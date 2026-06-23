package com.banking.observability.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReceivedEventJpaRepository extends JpaRepository<ReceivedEventEntity, UUID> {
    boolean existsByEventId(UUID eventId);
}
