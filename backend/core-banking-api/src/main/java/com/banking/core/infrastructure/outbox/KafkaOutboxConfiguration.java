package com.banking.core.infrastructure.outbox;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KafkaOutboxProperties.class)
public class KafkaOutboxConfiguration {
}
