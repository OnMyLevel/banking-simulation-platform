package com.banking.core.application.facade;

import com.banking.core.domain.exception.OutboxEventNotFoundException;
import com.banking.core.domain.model.OutboxEvent;
import com.banking.core.domain.model.OutboxEventStatus;
import com.banking.core.domain.repository.OutboxEventRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OutboxOpsFacadeTest {
    private final OutboxEventRepository repository = mock(OutboxEventRepository.class);
    private final OutboxOpsFacade facade = new OutboxOpsFacade(repository);

    @Test
    void shouldFindEventsByStatus() {
        OutboxEvent event = OutboxEvent.pending(UUID.randomUUID(), "CORE_OPERATION_COMPLETED", "OBSERVABILITY_HTTP", "{}");
        when(repository.findByStatus(OutboxEventStatus.FAILED, 25, 0)).thenReturn(List.of(event));

        List<OutboxEvent> events = facade.findByStatus(OutboxEventStatus.FAILED, 25, 0);

        assertThat(events).containsExactly(event);
    }

    @Test
    void shouldRetryEventNow() {
        UUID eventId = UUID.randomUUID();
        OutboxEvent event = new OutboxEvent(
            eventId,
            UUID.randomUUID(),
            "CORE_OPERATION_COMPLETED",
            "OBSERVABILITY_HTTP",
            OutboxEventStatus.FAILED,
            "{}",
            3,
            "failure",
            Instant.MAX,
            Instant.now(),
            null
        );
        when(repository.findById(eventId)).thenReturn(Optional.of(event));
        when(repository.persist(org.mockito.ArgumentMatchers.any(OutboxEvent.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OutboxEvent retried = facade.retryNow(eventId);

        assertThat(retried.status()).isEqualTo(OutboxEventStatus.PENDING);
        assertThat(retried.retryCount()).isEqualTo(3);
        assertThat(retried.nextRetryAt()).isBefore(Instant.now().plusSeconds(2));
        verify(repository).persist(org.mockito.ArgumentMatchers.any(OutboxEvent.class));
    }

    @Test
    void shouldFailWhenEventDoesNotExist() {
        UUID eventId = UUID.randomUUID();
        when(repository.findById(eventId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> facade.retryNow(eventId))
            .isInstanceOf(OutboxEventNotFoundException.class);
    }
}
