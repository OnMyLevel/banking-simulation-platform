package com.banking.gateway.access;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class GatewayClaimAuthoritiesConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {
    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
        return Mono.just(new JwtAuthenticationToken(jwt, authorities(jwt)));
    }

    private Collection<GrantedAuthority> authorities(Jwt jwt) {
        return Stream.concat(roles(jwt).stream(), scopeAuthorities(jwt).stream())
            .filter(Objects::nonNull)
            .distinct()
            .map(SimpleGrantedAuthority::new)
            .map(GrantedAuthority.class::cast)
            .toList();
    }

    private List<String> roles(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles == null) {
            return List.of();
        }
        return roles.stream()
            .filter(role -> role != null && !role.isBlank())
            .map(String::trim)
            .map(role -> role.startsWith(ROLE_PREFIX) ? role : ROLE_PREFIX + role)
            .toList();
    }

    private List<String> scopeAuthorities(Jwt jwt) {
        String scope = jwt.getClaimAsString("scope");
        if (scope == null || scope.isBlank()) {
            return List.of();
        }
        return Stream.of(scope.split(" "))
            .filter(value -> value != null && !value.isBlank())
            .map(value -> "SCOPE_" + value.trim())
            .toList();
    }
}
