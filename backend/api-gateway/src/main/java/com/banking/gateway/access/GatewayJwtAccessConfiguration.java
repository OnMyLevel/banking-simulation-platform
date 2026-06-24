package com.banking.gateway.access;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableConfigurationProperties(GatewayProviderProperties.class)
@Profile("jwt")
public class GatewayJwtAccessConfiguration {
    @Bean
    SecurityWebFilterChain gatewayJwtRules(ServerHttpSecurity http) {
        GatewayClaimAuthoritiesConverter converter = new GatewayClaimAuthoritiesConverter();
        return http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/actuator/health", "/actuator/info").permitAll()
                .pathMatchers("/internal/**").denyAll()
                .pathMatchers("/api/users/**").permitAll()
                .pathMatchers("/api/accounts/**").hasAnyRole(GatewayRoles.USER, GatewayRoles.ADVISOR, GatewayRoles.ADMIN)
                .pathMatchers("/api/operations/**").hasAnyRole(GatewayRoles.USER, GatewayRoles.ADVISOR, GatewayRoles.ADMIN)
                .anyExchange().denyAll()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(converter)))
            .build();
    }
}
