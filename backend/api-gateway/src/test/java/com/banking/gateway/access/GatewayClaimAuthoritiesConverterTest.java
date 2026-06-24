package com.banking.gateway.access;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GatewayClaimAuthoritiesConverterTest {
    private final GatewayClaimAuthoritiesConverter converter = new GatewayClaimAuthoritiesConverter();

    @Test
    void shouldMapRolesClaimToSpringAuthorities() {
        Authentication authentication = converter.convert(jwt(Map.of("roles", List.of("USER", "ADVISOR")))).block();

        assertThat(authentication.getAuthorities())
            .extracting("authority")
            .contains("ROLE_USER", "ROLE_ADVISOR");
    }

    @Test
    void shouldKeepAlreadyPrefixedRoles() {
        Authentication authentication = converter.convert(jwt(Map.of("roles", List.of("ROLE_ADMIN")))).block();

        assertThat(authentication.getAuthorities())
            .extracting("authority")
            .contains("ROLE_ADMIN");
    }

    @Test
    void shouldMapScopeClaimToScopeAuthorities() {
        Authentication authentication = converter.convert(jwt(Map.of("scope", "accounts:read operations:write"))).block();

        assertThat(authentication.getAuthorities())
            .extracting("authority")
            .contains("SCOPE_accounts:read", "SCOPE_operations:write");
    }

    private Jwt jwt(Map<String, Object> claims) {
        return new Jwt(
            "token-value",
            Instant.parse("2026-06-24T15:00:00Z"),
            Instant.parse("2026-06-24T16:00:00Z"),
            Map.of("alg", "none"),
            claims
        );
    }
}
