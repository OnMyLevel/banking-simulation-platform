package com.banking.gateway.access;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GatewayProviderPropertiesTest {
    @Test
    void shouldExposeDefaultProviderValues() {
        GatewayProviderProperties properties = new GatewayProviderProperties();

        assertThat(properties.name()).isEqualTo("local-provider");
        assertThat(properties.issuer()).isEqualTo("local-issuer");
        assertThat(properties.keysPath()).isEqualTo("/keys");
        assertThat(properties.audience()).isEqualTo("banking-api-gateway");
    }

    @Test
    void shouldAllowProviderValuesOverride() {
        GatewayProviderProperties properties = new GatewayProviderProperties();

        properties.setName("enterprise-provider");
        properties.setIssuer("enterprise-issuer");
        properties.setKeysPath("/enterprise-keys");
        properties.setAudience("enterprise-gateway");

        assertThat(properties.name()).isEqualTo("enterprise-provider");
        assertThat(properties.issuer()).isEqualTo("enterprise-issuer");
        assertThat(properties.keysPath()).isEqualTo("/enterprise-keys");
        assertThat(properties.audience()).isEqualTo("enterprise-gateway");
    }
}
