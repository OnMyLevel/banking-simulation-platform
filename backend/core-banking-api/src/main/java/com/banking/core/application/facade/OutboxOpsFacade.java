package com.banking.core.application.facade;

import com.banking.core.domain.exception.OutboxEventNotFoundException;
import com.banking.core.domain.model.OutboxEvent;
import com.banking.core.domain.model.OutboxEventStatus;
import com.banking.core.domain.repository.OutboxEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OutboxOpsFacade {
    private final OutboxEventRepository repository;

    public OutboxOpsFacade(OutboxEventRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<OutboxEvent> findByStatus(OutboxEventStatus status, int limit, int offset) {
        return repository.findByStatus(status, limit, offset);
    }

    @Transactional
    public OutboxEvent retryNow(UUID eventId) {
        OutboxEvent event = repository.findById(eventId)
            .orElseThrow(() -> new OutboxEventNotFoundException(eventId));
        return repository.persist(event.retryNow());
    }
}
