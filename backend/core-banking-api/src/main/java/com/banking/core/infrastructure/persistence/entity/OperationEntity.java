package com.banking.core.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "operations", schema = "core_schema")
public class OperationEntity {
    @Id
    public UUID id;

    public UUID sourceAccountId;
    public UUID targetAccountId;

    @Column(nullable = false, length = 40)
    public String kind;

    @Column(nullable = false, length = 40)
    public String status;

    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal amount;

    @Column(nullable = false, length = 3)
    public String currency;

    @Column(nullable = false, unique = true, length = 120)
    public String idempotencyKey;

    @Column(nullable = false)
    public Instant createdAt;
}
