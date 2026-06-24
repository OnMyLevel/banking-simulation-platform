package com.banking.core.infrastructure.outbox;

import com.banking.core.domain.model.OutboxEvent;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KafkaEventSenderTest {
    @Test
    void shouldSupportKafkaDestination() {
        KafkaTemplate<String, String> kafkaTemplate = mock(KafkaTemplate.class);
        KafkaEventSender sender = new KafkaEventSender(kafkaTemplate, new KafkaOutboxProperties("banking.core.events"));

        assertThat(sender.supports("KAFKA")).isTrue();
        assertThat(sender.supports("FLUENT_BIT")).isFalse();
    }

    @Test
    void shouldSendPayloadToKafkaTopic() {
        KafkaTemplate<String, String> kafkaTemplate = mock(KafkaTemplate.class);
        KafkaEventSender sender = new KafkaEventSender(kafkaTemplate, new KafkaOutboxProperties("banking.core.events"));
        UUID aggregateId = UUID.randomUUID();
        OutboxEvent event = OutboxEvent.pending(aggregateId, "CORE_OPERATION_COMPLETED", "KAFKA", "{\"eventKind\":\"CREDIT\"}");
        when(kafkaTemplate.send("banking.core.events", aggregateId.toString(), "{\"eventKind\":\"CREDIT\"}"))
            .thenReturn(CompletableFuture.completedFuture(null));

        sender.send(event);

        verify(kafkaTemplate).send("banking.core.events", aggregateId.toString(), "{\"eventKind\":\"CREDIT\"}");
    }
}
