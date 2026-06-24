package com.banking.gateway.traffic;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GatewayTrafficPropertiesTest {
    @Test
    void shouldUseLocalStoreByDefault() {
        GatewayTrafficProperties properties = new GatewayTrafficProperties();

        assertThat(properties.trafficStoreMode()).isEqualTo(TrafficStoreMode.IN_MEMORY);
    }

    @Test
    void shouldAllowRedisStoreMode() {
        GatewayTrafficProperties properties = new GatewayTrafficProperties();

        properties.setTrafficStoreMode(TrafficStoreMode.REDIS);

        assertThat(properties.trafficStoreMode()).isEqualTo(TrafficStoreMode.REDIS);
    }
}
