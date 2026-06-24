package com.banking.core.infrastructure.outbox;

import com.banking.core.domain.model.OutboxEvent;
import com.banking.core.domain.repository.OutboxEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@EnableConfigurationProperties(OutboxRetryProperties.class)
public class OutboxEventRelay {
    private static final Logger LOGGER = LoggerFactory.getLogger(OutboxEventRelay.class);
    private final OutboxEventRepository repository;
    private final EventDeliveryRouter router;
    private final OutboxRetryPolicy retryPolicy;

    public OutboxEventRelay(OutboxEventRepository repository, EventDeliveryRouter router, OutboxRetryProperties properties) {
        this.repository = repository;
        this.router = router;
        this.retryPolicy = new OutboxRetryPolicy(properties);
    }

    @Scheduled(fixedDelayString = "${banking.outbox.relay-delay:5000}")
    @Transactional
    public void relay() {
        repository.findPendingEvents(Instant.now(), retryPolicy.batchSize()).forEach(this::sendOne);
    }

    private void sendOne(OutboxEvent event) {
        try {
            router.send(event);
            repository.persist(event.sent());
        } catch (RuntimeException exception) {
            LOGGER.warn("Outbox event delivery failed for event {}", event.id());
            repository.persist(retryPolicy.failed(event, exception, Instant.now()));
        }
    }
}
