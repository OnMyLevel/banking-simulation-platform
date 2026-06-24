package com.banking.core.infrastructure.outbox;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(LogForwarderProperties.class)
public class LogForwarderConfiguration {
    @Bean
    @Qualifier("logForwarderRestClient")
    RestClient logForwarderRestClient(LogForwarderProperties properties) {
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
            .withConnectTimeout(properties.safeConnectTimeout())
            .withReadTimeout(properties.safeReadTimeout());
        return RestClient.builder()
            .baseUrl(properties.safeBaseUrl())
            .requestFactory(ClientHttpRequestFactories.get(settings))
            .build();
    }
}
