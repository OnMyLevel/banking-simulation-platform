package com.banking.observability.api.mapper;

import com.banking.observability.api.response.ReceivedEventResponse;
import com.banking.observability.domain.model.ReceivedEvent;

public final class EventApiMapper {
    private EventApiMapper() {
    }

    public static ReceivedEventResponse toResponse(ReceivedEvent event) {
        return new ReceivedEventResponse(
            event.id(),
            event.eventId(),
            event.sourceAccountId(),
            event.targetAccountId(),
            event.eventKind(),
            event.eventStatus(),
            event.amount(),
            event.currency(),
            event.eventKey(),
            event.occurredAt(),
            event.receivedAt()
        );
    }
}
