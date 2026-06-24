package com.banking.core.infrastructure.outbox;

import com.banking.core.domain.model.OutboxEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventDeliveryRouter {
    private final List<EventSender> senders;

    public EventDeliveryRouter(List<EventSender> senders) {
        this.senders = senders;
    }

    public void send(OutboxEvent event) {
        EventSender sender = senders.stream()
            .filter(candidate -> candidate.supports(event.destinationType()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No delivery sender for destination: " + event.destinationType()));
        sender.send(event);
    }
}
