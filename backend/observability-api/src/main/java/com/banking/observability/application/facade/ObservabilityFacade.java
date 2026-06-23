package com.banking.observability.application.facade;

import com.banking.observability.domain.model.ReceivedEvent;
import com.banking.observability.domain.repository.ReceivedEventRepository;
import com.banking.observability.domain.service.EventReceiverService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class ObservabilityFacade {
    private final EventReceiverService eventReceiverService;

    public ObservabilityFacade(ReceivedEventRepository repository) {
        this.eventReceiverService = new EventReceiverService(repository);
    }

    @Transactional
    public ReceivedEvent receive(UUID eventId, UUID sourceAccountId, UUID targetAccountId, String eventKind, String eventStatus, BigDecimal amount, String currency, String eventKey, Instant occurredAt) {
        return eventReceiverService.receive(eventId, sourceAccountId, targetAccountId, eventKind, eventStatus, amount, currency, eventKey, occurredAt);
    }
}
