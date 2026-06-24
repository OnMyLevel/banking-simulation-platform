package com.banking.core.infrastructure.outbox;

import com.banking.core.domain.model.OutboxEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventDeliveryRouter {
    private final List<EventSender> senders;
    private final OutboxDeliveryMetrics metrics;

    public EventDeliveryRouter(List<EventSender> senders, OutboxDeliveryMetrics metrics) {
        this.senders = senders;
        this.metrics = metrics;
    }

    public void send(OutboxEvent event) {
        EventSender sender = senders.stream()
            .filter(candidate -> candidate.supports(event.destinationType()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No delivery sender for destination: " + event.destinationType()));
        try {
            sender.send(event);
            metrics.recordSuccess(event);
        } catch (RuntimeException exception) {
            metrics.recordFailure(event);
            throw exception;
        }
    }
}
