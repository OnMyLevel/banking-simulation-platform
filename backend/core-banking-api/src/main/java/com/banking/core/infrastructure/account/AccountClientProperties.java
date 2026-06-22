package com.banking.core.infrastructure.account;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "banking.account-api")
public record AccountClientProperties(String baseUrl) {
}
