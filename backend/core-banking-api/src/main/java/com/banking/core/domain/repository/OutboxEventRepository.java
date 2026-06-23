package com.banking.core.domain.repository;

import com.banking.core.domain.model.OutboxEvent;

import java.time.Instant;
import java.util.List;

public interface OutboxEventRepository {
    OutboxEvent persist(OutboxEvent event);
    List<OutboxEvent> findPendingEvents(Instant now, int limit);
}
