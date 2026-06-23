package com.banking.observability.domain.service;

import com.banking.observability.domain.model.ReceivedEvent;
import com.banking.observability.domain.repository.ReceivedEventRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class EventReceiverService {
    private final ReceivedEventRepository repository;

    public EventReceiverService(ReceivedEventRepository repository) {
        this.repository = repository;
    }

    public ReceivedEvent receive(UUID eventId, UUID sourceAccountId, UUID targetAccountId, String eventKind, String eventStatus, BigDecimal amount, String currency, String eventKey, Instant occurredAt) {
        return repository.findByEventId(eventId)
            .orElseGet(() -> {
                ReceivedEvent event = ReceivedEvent.receive(eventId, sourceAccountId, targetAccountId, eventKind, eventStatus, amount, currency, eventKey, occurredAt);
                return repository.persist(event);
            });
    }
}
