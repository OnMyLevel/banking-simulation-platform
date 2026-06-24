package com.banking.core.infrastructure.outbox;

import com.banking.core.domain.model.OutboxEvent;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EventDeliveryRouterTest {
    @Test
    void shouldRouteEventToMatchingSender() {
        OutboxEvent event = OutboxEvent.pending(UUID.randomUUID(), "CORE_OPERATION_COMPLETED", "OBSERVABILITY_HTTP", "{}");
        EventSender httpSender = mock(EventSender.class);
        EventSender noopSender = mock(EventSender.class);
        when(httpSender.supports("OBSERVABILITY_HTTP")).thenReturn(true);
        when(noopSender.supports("OBSERVABILITY_HTTP")).thenReturn(false);
        EventDeliveryRouter router = new EventDeliveryRouter(List.of(noopSender, httpSender));

        router.send(event);

        verify(httpSender).send(event);
        verify(noopSender, never()).send(event);
    }

    @Test
    void shouldRejectUnknownDestination() {
        OutboxEvent event = OutboxEvent.pending(UUID.randomUUID(), "CORE_OPERATION_COMPLETED", "KAFKA", "{}");
        EventSender sender = mock(EventSender.class);
        when(sender.supports("KAFKA")).thenReturn(false);
        EventDeliveryRouter router = new EventDeliveryRouter(List.of(sender));

        assertThatThrownBy(() -> router.send(event))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("KAFKA");
    }
}
