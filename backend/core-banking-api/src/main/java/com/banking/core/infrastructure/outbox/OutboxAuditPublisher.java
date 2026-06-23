package com.banking.core.infrastructure.outbox;

import com.banking.core.domain.model.AuditEvent;
import com.banking.core.domain.model.OutboxEvent;
import com.banking.core.domain.port.AuditPublisher;
import com.banking.core.domain.repository.OutboxEventRepository;
import org.springframework.stereotype.Component;

@Component
public class OutboxAuditPublisher implements AuditPublisher {
    private static final String EVENT_TYPE = "CORE_OPERATION_COMPLETED";
    private static final String DESTINATION_TYPE = "OBSERVABILITY_HTTP";
    private final OutboxEventRepository repository;

    public OutboxAuditPublisher(OutboxEventRepository repository) {
        this.repository = repository;
    }

    @Override
    public void publish(AuditEvent event) {
        repository.persist(OutboxEvent.pending(
            event.operationId(),
            EVENT_TYPE,
            DESTINATION_TYPE,
            toPayload(event)
        ));
    }

    private String toPayload(AuditEvent event) {
        return "{" +
            "\"eventId\":\"" + event.operationId() + "\"," +
            "\"sourceAccountId\":" + jsonUuid(event.sourceAccountId()) + "," +
            "\"targetAccountId\":" + jsonUuid(event.targetAccountId()) + "," +
            "\"eventKind\":\"" + event.operationKind().name() + "\"," +
            "\"eventStatus\":\"" + event.operationStatus().name() + "\"," +
            "\"amount\":" + event.amount() + "," +
            "\"currency\":\"" + event.currency() + "\"," +
            "\"eventKey\":\"" + event.idempotencyKey() + "\"," +
            "\"occurredAt\":\"" + event.occurredAt() + "\"" +
            "}";
    }

    private String jsonUuid(java.util.UUID value) {
        return value == null ? "null" : "\"" + value + "\"";
    }
}
