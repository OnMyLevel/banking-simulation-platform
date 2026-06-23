package com.banking.observability.domain.repository;

import com.banking.observability.domain.model.ReceivedEvent;

import java.util.Optional;
import java.util.UUID;

public interface ReceivedEventRepository {
    ReceivedEvent persist(ReceivedEvent event);
    Optional<ReceivedEvent> findByEventId(UUID eventId);
    boolean existsByEventId(UUID eventId);
}
