package com.banking.core.infrastructure.outbox;

import com.banking.core.domain.model.OutboxEvent;

public final class OutboxEventMapper {
    private OutboxEventMapper() {
    }

    public static OutboxEventEntity toEntity(OutboxEvent event) {
        OutboxEventEntity entity = new OutboxEventEntity();
        entity.setId(event.id());
        entity.setAggregateId(event.aggregateId());
        entity.setEventType(event.eventType());
        entity.setDestinationType(event.destinationType());
        entity.setStatus(event.status());
        entity.setPayload(event.payload());
        entity.setRetryCount(event.retryCount());
        entity.setLastError(event.lastError());
        entity.setNextRetryAt(event.nextRetryAt());
        entity.setCreatedAt(event.createdAt());
        entity.setSentAt(event.sentAt());
        return entity;
    }

    public static OutboxEvent toDomain(OutboxEventEntity entity) {
        return new OutboxEvent(
            entity.getId(),
            entity.getAggregateId(),
            entity.getEventType(),
            entity.getDestinationType(),
            entity.getStatus(),
            entity.getPayload(),
            entity.getRetryCount(),
            entity.getLastError(),
            entity.getNextRetryAt(),
            entity.getCreatedAt(),
            entity.getSentAt()
        );
    }
}
