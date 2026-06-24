package com.banking.gateway.route;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.profiles.active=jwt",
        "BANKING_USER_API_URI=http://localhost:1",
        "BANKING_ACCOUNT_API_URI=http://localhost:1",
        "BANKING_CORE_API_URI=http://localhost:1"
    }
)
class GatewaySignedAccessRouteBehaviorTest {
    private static final KeyPair KEY_PAIR = generateKeyPair();
    private final WebTestClient webTestClient;

    GatewaySignedAccessRouteBehaviorTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @Test
    void shouldRejectAccountRouteWithoutBearer() {
        webTestClient.get()
            .uri("/api/accounts/accounts/123")
            .exchange()
            .expectStatus().isUnauthorized();
    }

    @Test
    void shouldAllowAccountRouteWithSignedUserAccess() {
        webTestClient.get()
            .uri("/api/accounts/accounts/123")
            .headers(headers -> headers.setBearerAuth(signedAccess(List.of("USER"))))
            .exchange()
            .expectStatus().is5xxServerError();
    }

    @Test
    void shouldAllowOperationRouteWithSignedAdvisorAccess() {
        webTestClient.post()
            .uri("/api/operations/operations/credits")
            .headers(headers -> headers.setBearerAuth(signedAccess(List.of("ADVISOR"))))
            .exchange()
            .expectStatus().is5xxServerError();
    }

    @Test
    void shouldRejectOperationRouteWithSignedOpsAccess() {
        webTestClient.post()
            .uri("/api/operations/operations/credits")
            .headers(headers -> headers.setBearerAuth(signedAccess(List.of("OPS"))))
            .exchange()
            .expectStatus().isForbidden();
    }

    private static String signedAccess(List<String> roles) {
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) KEY_PAIR.getPublic())
            .privateKey((RSAPrivateKey) KEY_PAIR.getPrivate())
            .keyID(UUID.randomUUID().toString())
            .build();
        NimbusJwtEncoder encoder = new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(rsaKey)));
        Instant now = Instant.parse("2026-06-24T15:00:00Z");
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("local-issuer")
            .subject("user-1")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(3600))
            .claim("roles", roles)
            .build();
        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256).build();
        return encoder.encode(org.springframework.security.oauth2.jwt.JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    private static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            return generator.generateKeyPair();
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }

    @TestConfiguration
    static class SignedAccessTestConfiguration {
        @Bean
        ReactiveJwtDecoder reactiveJwtDecoder() {
            return NimbusReactiveJwtDecoder.withPublicKey((RSAPublicKey) KEY_PAIR.getPublic()).build();
        }
    }
}
