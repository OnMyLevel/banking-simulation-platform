package com.banking.core.domain.model;

public enum OutboxEventStatus {
    PENDING,
    SENT,
    FAILED
}
