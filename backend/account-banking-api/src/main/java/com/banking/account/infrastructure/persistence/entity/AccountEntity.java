package com.banking.account.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "accounts", schema = "account_schema")
public class AccountEntity {
    @Id
    public UUID id;

    @Column(nullable = false)
    public UUID ownerId;

    @Column(nullable = false, unique = true, length = 34)
    public String iban;

    @Column(nullable = false, length = 40)
    public String status;

    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal balanceAmount;

    @Column(nullable = false, length = 3)
    public String currency;

    @Column(nullable = false)
    public Instant createdAt;

    @Column(nullable = false)
    public Instant updatedAt;
}
