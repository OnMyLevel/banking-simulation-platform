package com.banking.gateway.access;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@Profile("!jwt")
public class GatewayRouteRulesConfiguration {
    @Bean
    SecurityWebFilterChain gatewayRouteRules(ServerHttpSecurity http) {
        return http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/actuator/health", "/actuator/info").permitAll()
                .pathMatchers("/internal/**").denyAll()
                .pathMatchers("/api/users/**").permitAll()
                .pathMatchers("/api/accounts/**", "/api/operations/**").authenticated()
                .anyExchange().denyAll()
            )
            .build();
    }
}
