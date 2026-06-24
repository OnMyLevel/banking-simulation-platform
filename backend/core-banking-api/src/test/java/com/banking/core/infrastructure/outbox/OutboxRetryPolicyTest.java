package com.banking.core.infrastructure.outbox;

import com.banking.core.domain.model.OutboxEvent;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OutboxRetryPolicyTest {
    @Test
    void shouldCalculateNextRetryDate() {
        OutboxRetryPolicy policy = new OutboxRetryPolicy(new OutboxRetryProperties(Duration.ofSeconds(30), 5, 10));
        Instant now = Instant.parse("2026-06-24T08:00:00Z");
        OutboxEvent event = OutboxEvent.pending(UUID.randomUUID(), "CORE_OPERATION_COMPLETED", "OBSERVABILITY_HTTP", "{}");

        OutboxEvent failed = policy.failed(event, new RuntimeException("temporary error"), now);

        assertThat(failed.retryCount()).isEqualTo(1);
        assertThat(failed.lastError()).isEqualTo("temporary error");
        assertThat(failed.nextRetryAt()).isEqualTo(now.plusSeconds(30));
    }

    @Test
    void shouldUseSafeDefaultsWhenConfigurationIsMissing() {
        OutboxRetryPolicy policy = new OutboxRetryPolicy(new OutboxRetryProperties(null, 0, 0));
        Instant now = Instant.parse("2026-06-24T08:00:00Z");
        OutboxEvent event = OutboxEvent.pending(UUID.randomUUID(), "CORE_OPERATION_COMPLETED", "OBSERVABILITY_HTTP", "{}");

        OutboxEvent failed = policy.failed(event, new RuntimeException(), now);

        assertThat(policy.batchSize()).isEqualTo(25);
        assertThat(failed.retryCount()).isEqualTo(1);
        assertThat(failed.lastError()).isEqualTo("RuntimeException");
        assertThat(failed.nextRetryAt()).isEqualTo(now.plus(Duration.ofMinutes(1)));
    }

    @Test
    void shouldStopRetryingAfterMaxRetryCount() {
        OutboxRetryPolicy policy = new OutboxRetryPolicy(new OutboxRetryProperties(Duration.ofSeconds(30), 2, 10));
        Instant now = Instant.parse("2026-06-24T08:00:00Z");
        OutboxEvent event = OutboxEvent.pending(UUID.randomUUID(), "CORE_OPERATION_COMPLETED", "OBSERVABILITY_HTTP", "{}");
        OutboxEvent firstFailure = policy.failed(event, new RuntimeException("first"), now);

        OutboxEvent secondFailure = policy.failed(firstFailure, new RuntimeException("second"), now);

        assertThat(secondFailure.retryCount()).isEqualTo(2);
        assertThat(secondFailure.lastError()).isEqualTo("second");
        assertThat(secondFailure.nextRetryAt()).isEqualTo(Instant.MAX);
    }
}
