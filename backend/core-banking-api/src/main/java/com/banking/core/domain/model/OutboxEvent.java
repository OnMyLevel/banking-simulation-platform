package com.banking.core.domain.model;

import java.time.Instant;
import java.util.UUID;

public class OutboxEvent {
    private final UUID id;
    private final UUID aggregateId;
    private final String eventType;
    private final String destinationType;
    private final OutboxEventStatus status;
    private final String payload;
    private final int retryCount;
    private final String lastError;
    private final Instant nextRetryAt;
    private final Instant createdAt;
    private final Instant sentAt;

    public OutboxEvent(UUID id, UUID aggregateId, String eventType, String destinationType, OutboxEventStatus status, String payload, int retryCount, String lastError, Instant nextRetryAt, Instant createdAt, Instant sentAt) {
        this.id = id;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.destinationType = destinationType;
        this.status = status;
        this.payload = payload;
        this.retryCount = retryCount;
        this.lastError = lastError;
        this.nextRetryAt = nextRetryAt;
        this.createdAt = createdAt;
        this.sentAt = sentAt;
    }

    public static OutboxEvent pending(UUID aggregateId, String eventType, String destinationType, String payload) {
        Instant now = Instant.now();
        return new OutboxEvent(UUID.randomUUID(), aggregateId, eventType, destinationType, OutboxEventStatus.PENDING, payload, 0, null, now, now, null);
    }

    public OutboxEvent sent() {
        return new OutboxEvent(id, aggregateId, eventType, destinationType, OutboxEventStatus.SENT, payload, retryCount, lastError, nextRetryAt, createdAt, Instant.now());
    }

    public OutboxEvent failed(String error, Instant nextRetryAt) {
        String safeError = error == null ? "unknown" : error.substring(0, Math.min(error.length(), 500));
        return new OutboxEvent(id, aggregateId, eventType, destinationType, OutboxEventStatus.FAILED, payload, retryCount + 1, safeError, nextRetryAt, createdAt, sentAt);
    }

    public OutboxEvent retryNow() {
        return new OutboxEvent(id, aggregateId, eventType, destinationType, OutboxEventStatus.PENDING, payload, retryCount, lastError, Instant.now(), createdAt, sentAt);
    }

    public UUID id() { return id; }
    public UUID aggregateId() { return aggregateId; }
    public String eventType() { return eventType; }
    public String destinationType() { return destinationType; }
    public OutboxEventStatus status() { return status; }
    public String payload() { return payload; }
    public int retryCount() { return retryCount; }
    public String lastError() { return lastError; }
    public Instant nextRetryAt() { return nextRetryAt; }
    public Instant createdAt() { return createdAt; }
    public Instant sentAt() { return sentAt; }
}
