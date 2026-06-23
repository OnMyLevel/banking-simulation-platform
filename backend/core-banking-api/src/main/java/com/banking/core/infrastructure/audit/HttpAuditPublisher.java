package com.banking.core.infrastructure.audit;

import com.banking.core.domain.model.AuditEvent;
import com.banking.core.domain.port.AuditPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class HttpAuditPublisher implements AuditPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpAuditPublisher.class);
    private final RestClient auditRestClient;

    public HttpAuditPublisher(RestClient auditRestClient) {
        this.auditRestClient = auditRestClient;
    }

    @Override
    public void publish(AuditEvent event) {
        try {
            auditRestClient.post()
                .uri("/events")
                .body(toPayload(event))
                .retrieve()
                .toBodilessEntity();
        } catch (RestClientException exception) {
            LOGGER.warn("Event delivery failed for operation {}", event.operationId());
        }
    }

    private EventPayload toPayload(AuditEvent event) {
        return new EventPayload(
            event.operationId(),
            event.sourceAccountId(),
            event.targetAccountId(),
            event.operationKind().name(),
            event.operationStatus().name(),
            event.amount(),
            event.currency(),
            event.idempotencyKey(),
            event.occurredAt()
        );
    }
}
