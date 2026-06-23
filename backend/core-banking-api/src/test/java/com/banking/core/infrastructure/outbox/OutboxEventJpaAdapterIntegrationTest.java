package com.banking.core.infrastructure.outbox;

import com.banking.core.domain.model.OutboxEvent;
import com.banking.core.domain.model.OutboxEventStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@Import(OutboxEventJpaAdapter.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OutboxEventJpaAdapterIntegrationTest {
    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
        .withDatabaseName("banking_simulation")
        .withUsername("banking")
        .withPassword("banking");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.flyway.schemas", () -> "core_schema");
        registry.add("spring.flyway.default-schema", () -> "core_schema");
    }

    @Autowired
    private OutboxEventJpaAdapter adapter;

    @Test
    void shouldPersistOutboxEventWithRetryMetadata() {
        Instant now = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        OutboxEvent event = new OutboxEvent(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "CORE_OPERATION_COMPLETED",
            "OBSERVABILITY_HTTP",
            OutboxEventStatus.FAILED,
            "{\"eventKind\":\"TRANSFER\"}",
            2,
            "temporary failure",
            now.plus(1, ChronoUnit.MINUTES),
            now.minus(1, ChronoUnit.MINUTES),
            null
        );

        OutboxEvent persisted = adapter.persist(event);

        assertThat(persisted.id()).isEqualTo(event.id());
        assertThat(persisted.aggregateId()).isEqualTo(event.aggregateId());
        assertThat(persisted.eventType()).isEqualTo("CORE_OPERATION_COMPLETED");
        assertThat(persisted.destinationType()).isEqualTo("OBSERVABILITY_HTTP");
        assertThat(persisted.status()).isEqualTo(OutboxEventStatus.FAILED);
        assertThat(persisted.retryCount()).isEqualTo(2);
        assertThat(persisted.lastError()).isEqualTo("temporary failure");
        assertThat(persisted.nextRetryAt()).isEqualTo(event.nextRetryAt());
    }

    @Test
    void shouldFindOnlyPendingAndFailedEventsReadyForRetryOrderedByCreationDate() {
        Instant now = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        OutboxEvent olderFailed = new OutboxEvent(
            UUID.randomUUID(), UUID.randomUUID(), "CORE_OPERATION_COMPLETED", "OBSERVABILITY_HTTP",
            OutboxEventStatus.FAILED, "{\"eventKind\":\"DEBIT\"}", 1, "timeout",
            now.minus(1, ChronoUnit.MINUTES), now.minus(5, ChronoUnit.MINUTES), null
        );
        OutboxEvent newerPending = new OutboxEvent(
            UUID.randomUUID(), UUID.randomUUID(), "CORE_OPERATION_COMPLETED", "OBSERVABILITY_HTTP",
            OutboxEventStatus.PENDING, "{\"eventKind\":\"CREDIT\"}", 0, null,
            now.minus(1, ChronoUnit.MINUTES), now.minus(2, ChronoUnit.MINUTES), null
        );
        OutboxEvent sent = new OutboxEvent(
            UUID.randomUUID(), UUID.randomUUID(), "CORE_OPERATION_COMPLETED", "OBSERVABILITY_HTTP",
            OutboxEventStatus.SENT, "{\"eventKind\":\"TRANSFER\"}", 0, null,
            now.minus(1, ChronoUnit.MINUTES), now.minus(4, ChronoUnit.MINUTES), now.minus(30, ChronoUnit.SECONDS)
        );
        OutboxEvent futurePending = new OutboxEvent(
            UUID.randomUUID(), UUID.randomUUID(), "CORE_OPERATION_COMPLETED", "OBSERVABILITY_HTTP",
            OutboxEventStatus.PENDING, "{\"eventKind\":\"TRANSFER\"}", 0, null,
            now.plus(5, ChronoUnit.MINUTES), now.minus(3, ChronoUnit.MINUTES), null
        );

        adapter.persist(newerPending);
        adapter.persist(sent);
        adapter.persist(futurePending);
        adapter.persist(olderFailed);

        List<OutboxEvent> readyEvents = adapter.findPendingEvents(now, 10);

        assertThat(readyEvents).extracting(OutboxEvent::id)
            .containsExactly(olderFailed.id(), newerPending.id());
        assertThat(readyEvents).extracting(OutboxEvent::status)
            .containsExactly(OutboxEventStatus.FAILED, OutboxEventStatus.PENDING);
    }
}
