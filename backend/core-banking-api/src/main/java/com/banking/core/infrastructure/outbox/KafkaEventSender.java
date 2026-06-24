package com.banking.core.infrastructure.outbox;

import com.banking.core.domain.model.OutboxEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventSender implements EventSender {
    private static final String DESTINATION_TYPE = "KAFKA";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaOutboxProperties properties;

    public KafkaEventSender(KafkaTemplate<String, String> kafkaTemplate, KafkaOutboxProperties properties) {
        this.kafkaTemplate = kafkaTemplate;
        this.properties = properties;
    }

    @Override
    public boolean supports(String destinationType) {
        return DESTINATION_TYPE.equals(destinationType);
    }

    @Override
    public void send(OutboxEvent event) {
        kafkaTemplate.send(properties.safeTopic(), event.aggregateId().toString(), event.payload()).join();
    }
}
