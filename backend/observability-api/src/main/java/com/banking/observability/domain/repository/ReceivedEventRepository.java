package com.banking.observability.domain.repository;

import com.banking.observability.domain.model.ReceivedEvent;

import java.util.UUID;

public interface ReceivedEventRepository {
    ReceivedEvent persist(ReceivedEvent event);
    boolean existsByEventId(UUID eventId);
}
