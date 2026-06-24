package com.banking.core.infrastructure.outbox;

import com.banking.core.domain.model.AuditEvent;
import com.banking.core.domain.model.OperationKind;
import com.banking.core.domain.model.OperationStatus;
import com.banking.core.domain.model.OutboxEvent;
import com.banking.core.domain.repository.OutboxEventRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class OutboxAuditPublisherTest {
    private final OutboxEventRepository repository = mock(OutboxEventRepository.class);

    @Test
    void shouldUseDefaultHttpDestinationWhenConfigurationIsMissing() {
        OutboxAuditPublisher publisher = new OutboxAuditPublisher(repository, new OutboxDestinationProperties(null));

        publisher.publish(auditEvent());

        OutboxEvent event = capturedEvent();
        assertThat(event.destinationType()).isEqualTo("OBSERVABILITY_HTTP");
    }

    @Test
    void shouldUseConfiguredKafkaDestination() {
        OutboxAuditPublisher publisher = new OutboxAuditPublisher(repository, new OutboxDestinationProperties("KAFKA"));

        publisher.publish(auditEvent());

        OutboxEvent event = capturedEvent();
        assertThat(event.destinationType()).isEqualTo("KAFKA");
    }

    @Test
    void shouldUseConfiguredFluentBitDestination() {
        OutboxAuditPublisher publisher = new OutboxAuditPublisher(repository, new OutboxDestinationProperties("FLUENT_BIT"));

        publisher.publish(auditEvent());

        OutboxEvent event = capturedEvent();
        assertThat(event.destinationType()).isEqualTo("FLUENT_BIT");
    }

    private OutboxEvent capturedEvent() {
        ArgumentCaptor<OutboxEvent> captor = ArgumentCaptor.forClass(OutboxEvent.class);
        verify(repository).persist(captor.capture());
        return captor.getValue();
    }

    private AuditEvent auditEvent() {
        return new AuditEvent(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            OperationKind.CREDIT,
            OperationStatus.COMPLETED,
            BigDecimal.TEN,
            "EUR",
            "operation-key",
            Instant.parse("2026-06-24T12:00:00Z")
        );
    }
}
