package com.banking.observability.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_events", schema = "observability_schema")
public class ReceivedEventEntity {
    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID eventId;
    private UUID sourceAccountId;
    private UUID targetAccountId;
    @Column(nullable = false)
    private String eventKind;
    @Column(nullable = false)
    private String eventStatus;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    @Column(nullable = false, length = 3)
    private String currency;
    @Column(nullable = false, length = 128)
    private String eventKey;
    @Column(nullable = false)
    private Instant occurredAt;
    @Column(nullable = false)
    private Instant receivedAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }
    public UUID getSourceAccountId() { return sourceAccountId; }
    public void setSourceAccountId(UUID sourceAccountId) { this.sourceAccountId = sourceAccountId; }
    public UUID getTargetAccountId() { return targetAccountId; }
    public void setTargetAccountId(UUID targetAccountId) { this.targetAccountId = targetAccountId; }
    public String getEventKind() { return eventKind; }
    public void setEventKind(String eventKind) { this.eventKind = eventKind; }
    public String getEventStatus() { return eventStatus; }
    public void setEventStatus(String eventStatus) { this.eventStatus = eventStatus; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getEventKey() { return eventKey; }
    public void setEventKey(String eventKey) { this.eventKey = eventKey; }
    public Instant getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Instant occurredAt) { this.occurredAt = occurredAt; }
    public Instant getReceivedAt() { return receivedAt; }
    public void setReceivedAt(Instant receivedAt) { this.receivedAt = receivedAt; }
}
