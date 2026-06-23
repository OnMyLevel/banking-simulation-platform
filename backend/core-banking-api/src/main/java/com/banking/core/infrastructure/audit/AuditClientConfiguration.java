package com.banking.core.infrastructure.audit;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(AuditClientProperties.class)
public class AuditClientConfiguration {
    @Bean
    RestClient auditRestClient(AuditClientProperties properties) {
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
            .withConnectTimeout(properties.connectTimeout())
            .withReadTimeout(properties.readTimeout());
        return RestClient.builder()
            .baseUrl(properties.baseUrl())
            .requestFactory(ClientHttpRequestFactories.get(settings))
            .build();
    }
}
