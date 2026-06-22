package com.banking.core.infrastructure.account;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "banking.account-api")
public record AccountClientProperties(
    String baseUrl,
    Duration connectTimeout,
    Duration readTimeout
) {
}
