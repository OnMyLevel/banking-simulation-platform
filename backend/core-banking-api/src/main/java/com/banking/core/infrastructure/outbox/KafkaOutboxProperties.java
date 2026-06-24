package com.banking.core.infrastructure.outbox;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "banking.kafka-outbox")
public record KafkaOutboxProperties(
    String topic
) {
    public String safeTopic() {
        return topic == null || topic.isBlank() ? "banking.core.events" : topic;
    }
}
