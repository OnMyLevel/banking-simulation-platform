package com.banking.core.infrastructure.outbox;

import com.banking.core.domain.model.OutboxEventStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events", schema = "core_schema")
public class OutboxEventEntity {
    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID aggregateId;
    @Column(nullable = false, length = 80)
    private String eventType;
    @Column(nullable = false, length = 40)
    private String destinationType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OutboxEventStatus status;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;
    @Column(nullable = false)
    private int retryCount;
    @Column(length = 500)
    private String lastError;
    @Column(nullable = false)
    private Instant nextRetryAt;
    @Column(nullable = false)
    private Instant createdAt;
    private Instant sentAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getAggregateId() { return aggregateId; }
    public void setAggregateId(UUID aggregateId) { this.aggregateId = aggregateId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getDestinationType() { return destinationType; }
    public void setDestinationType(String destinationType) { this.destinationType = destinationType; }
    public OutboxEventStatus getStatus() { return status; }
    public void setStatus(OutboxEventStatus status) { this.status = status; }
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
    public String getLastError() { return lastError; }
    public void setLastError(String lastError) { this.lastError = lastError; }
    public Instant getNextRetryAt() { return nextRetryAt; }
    public void setNextRetryAt(Instant nextRetryAt) { this.nextRetryAt = nextRetryAt; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getSentAt() { return sentAt; }
    public void setSentAt(Instant sentAt) { this.sentAt = sentAt; }
}
