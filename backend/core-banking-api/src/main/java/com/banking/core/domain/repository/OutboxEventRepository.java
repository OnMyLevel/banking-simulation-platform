package com.banking.core.domain.repository;

import com.banking.core.domain.model.OutboxEvent;
import com.banking.core.domain.model.OutboxEventStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OutboxEventRepository {
    OutboxEvent persist(OutboxEvent event);
    List<OutboxEvent> findPendingEvents(Instant now, int limit);
    List<OutboxEvent> findByStatus(OutboxEventStatus status, int limit, int offset);
    Optional<OutboxEvent> findById(UUID eventId);
}
