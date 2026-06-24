package com.banking.gateway.route;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.profiles.active=jwt",
        "BANKING_USER_API_URI=http://localhost:1",
        "BANKING_ACCOUNT_API_URI=http://localhost:1",
        "BANKING_CORE_API_URI=http://localhost:1"
    }
)
class GatewayJwtRouteBehaviorTest {
    private final WebTestClient webTestClient;

    GatewayJwtRouteBehaviorTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @Test
    void shouldExposeHealthEndpointPubliclyInJwtProfile() {
        webTestClient.get()
            .uri("/actuator/health")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void shouldRejectAccountRouteWithoutJwt() {
        webTestClient.get()
            .uri("/api/accounts/accounts/123")
            .exchange()
            .expectStatus().isUnauthorized();
    }

    @Test
    void shouldAllowAccountRouteWithUserRole() {
        webTestClient.mutateWith(mockJwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .get()
            .uri("/api/accounts/accounts/123")
            .exchange()
            .expectStatus().is5xxServerError();
    }

    @Test
    void shouldAllowOperationRouteWithAdvisorRole() {
        webTestClient.mutateWith(mockJwt().authorities(new SimpleGrantedAuthority("ROLE_ADVISOR")))
            .post()
            .uri("/api/operations/operations/credits")
            .exchange()
            .expectStatus().is5xxServerError();
    }

    @Test
    void shouldRejectOperationRouteWithOpsRole() {
        webTestClient.mutateWith(mockJwt().authorities(new SimpleGrantedAuthority("ROLE_OPS")))
            .post()
            .uri("/api/operations/operations/credits")
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test
    void shouldRejectInternalRouteEvenWithAdminRole() {
        webTestClient.mutateWith(mockJwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
            .get()
            .uri("/internal/outbox-events")
            .exchange()
            .expectStatus().isForbidden();
    }

    @TestConfiguration
    static class JwtTestConfiguration {
        @Bean
        ReactiveJwtDecoder reactiveJwtDecoder() {
            return token -> Mono.error(new IllegalStateException("Not used by mockJwt tests"));
        }
    }
}
