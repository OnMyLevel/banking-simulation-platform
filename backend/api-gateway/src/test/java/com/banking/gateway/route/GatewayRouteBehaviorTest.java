package com.banking.gateway.route;

import com.banking.gateway.filter.CorrelationIdFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "BANKING_USER_API_URI=http://localhost:1",
        "BANKING_ACCOUNT_API_URI=http://localhost:1",
        "BANKING_CORE_API_URI=http://localhost:1"
    }
)
class GatewayRouteBehaviorTest {
    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldExposeHealthEndpointPublicly() {
        webTestClient.get()
            .uri("/actuator/health")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void shouldRejectInternalRoutesAtGatewayBoundary() {
        webTestClient.get()
            .uri("/internal/outbox-events")
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test
    void shouldKeepUserRoutesPublicForMvp() {
        webTestClient.get()
            .uri("/api/users/health-check")
            .exchange()
            .expectStatus().is5xxServerError();
    }

    @Test
    void shouldProtectCleanAccountRoutes() {
        webTestClient.get()
            .uri("/api/accounts/123")
            .exchange()
            .expectStatus().isUnauthorized();
    }

    @Test
    void shouldProtectCleanOperationRoutes() {
        webTestClient.post()
            .uri("/api/operations/credits")
            .exchange()
            .expectStatus().isUnauthorized();
    }

    @Test
    void shouldReturnExistingCorrelationIdOnPublicRoute() {
        webTestClient.get()
            .uri("/api/users/health-check")
            .header(CorrelationIdFilter.CORRELATION_ID_HEADER, "correlation-123")
            .exchange()
            .expectHeader().valueEquals(CorrelationIdFilter.CORRELATION_ID_HEADER, "correlation-123");
    }

    @Test
    void shouldGenerateCorrelationIdOnPublicRoute() {
        webTestClient.get()
            .uri("/api/users/health-check")
            .exchange()
            .expectHeader().exists(CorrelationIdFilter.CORRELATION_ID_HEADER);
    }
}
