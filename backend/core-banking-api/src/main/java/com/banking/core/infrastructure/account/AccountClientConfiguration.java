package com.banking.core.infrastructure.account;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(AccountClientProperties.class)
public class AccountClientConfiguration {

    @Bean
    RestClient accountRestClient(AccountClientProperties properties) {
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
            .withConnectTimeout(properties.connectTimeout())
            .withReadTimeout(properties.readTimeout());
        return RestClient.builder()
            .baseUrl(properties.baseUrl())
            .requestFactory(ClientHttpRequestFactories.get(settings))
            .build();
    }
}
