package com.banking.gateway.route;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = "spring.profiles.active=jwt"
)
class GatewayRouteForwardingTest {
    private static StubBackend userBackend;
    private static StubBackend accountBackend;
    private static StubBackend coreBackend;

    private final WebTestClient webTestClient;

    GatewayRouteForwardingTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @BeforeAll
    static void startBackends() throws IOException {
        userBackend = StubBackend.start();
        accountBackend = StubBackend.start();
        coreBackend = StubBackend.start();
    }

    @AfterAll
    static void stopBackends() {
        userBackend.stop();
        accountBackend.stop();
        coreBackend.stop();
    }

    @DynamicPropertySource
    static void gatewayTargets(DynamicPropertyRegistry registry) {
        registry.add("BANKING_USER_API_URI", () -> userBackend.baseUrl());
        registry.add("BANKING_ACCOUNT_API_URI", () -> accountBackend.baseUrl());
        registry.add("BANKING_CORE_API_URI", () -> coreBackend.baseUrl());
    }

    @Test
    void shouldForwardUserRouteToBackendWithoutGatewayPrefix() {
        webTestClient.get()
            .uri("/api/users/profile")
            .exchange()
            .expectStatus().isOk();

        assertThat(userBackend.lastPath()).isEqualTo("/profile");
    }

    @Test
    void shouldForwardCleanAccountRouteToBackendAccountPath() {
        webTestClient.mutateWith(mockJwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
            .get()
            .uri("/api/accounts/123")
            .exchange()
            .expectStatus().isOk();

        assertThat(accountBackend.lastPath()).isEqualTo("/accounts/123");
    }

    @Test
    void shouldForwardCleanOperationRouteToBackendOperationPath() {
        webTestClient.mutateWith(mockJwt().authorities(new SimpleGrantedAuthority("ROLE_ADVISOR")))
            .post()
            .uri("/api/operations/credits")
            .exchange()
            .expectStatus().isOk();

        assertThat(coreBackend.lastPath()).isEqualTo("/operations/credits");
    }

    @TestConfiguration
    static class ForwardingTestConfiguration {
        @Bean
        ReactiveJwtDecoder reactiveJwtDecoder() {
            return token -> Mono.error(new IllegalStateException("Not used by mockJwt tests"));
        }
    }

    private static final class StubBackend {
        private final HttpServer server;
        private final AtomicReference<String> lastPath = new AtomicReference<>();

        private StubBackend(HttpServer server) {
            this.server = server;
        }

        static StubBackend start() throws IOException {
            HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
            StubBackend backend = new StubBackend(server);
            server.createContext("/", backend::handle);
            server.start();
            return backend;
        }

        void handle(HttpExchange exchange) throws IOException {
            lastPath.set(exchange.getRequestURI().getPath());
            byte[] body = "ok".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        }

        String baseUrl() {
            return "http://localhost:" + server.getAddress().getPort();
        }

        String lastPath() {
            return lastPath.get();
        }

        void stop() {
            server.stop(0);
        }
    }
}
