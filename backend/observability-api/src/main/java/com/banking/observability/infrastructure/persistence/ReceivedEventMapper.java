package com.banking.observability.infrastructure.persistence;

import com.banking.observability.domain.model.ReceivedEvent;

public final class ReceivedEventMapper {
    private ReceivedEventMapper() {
    }

    public static ReceivedEventEntity toEntity(ReceivedEvent event) {
        ReceivedEventEntity entity = new ReceivedEventEntity();
        entity.setId(event.id());
        entity.setEventId(event.eventId());
        entity.setSourceAccountId(event.sourceAccountId());
        entity.setTargetAccountId(event.targetAccountId());
        entity.setEventKind(event.eventKind());
        entity.setEventStatus(event.eventStatus());
        entity.setAmount(event.amount());
        entity.setCurrency(event.currency());
        entity.setEventKey(event.eventKey());
        entity.setOccurredAt(event.occurredAt());
        entity.setReceivedAt(event.receivedAt());
        return entity;
    }

    public static ReceivedEvent toDomain(ReceivedEventEntity entity) {
        return new ReceivedEvent(
            entity.getId(),
            entity.getEventId(),
            entity.getSourceAccountId(),
            entity.getTargetAccountId(),
            entity.getEventKind(),
            entity.getEventStatus(),
            entity.getAmount(),
            entity.getCurrency(),
            entity.getEventKey(),
            entity.getOccurredAt(),
            entity.getReceivedAt()
        );
    }
}
