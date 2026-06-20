package com.banking.account.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Account {
    private final UUID id;
    private final UUID ownerId;
    private final String iban;
    private final AccountStatus status;
    private final Money balance;
    private final Instant createdAt;
    private final Instant updatedAt;

    public Account(UUID id, UUID ownerId, String iban, AccountStatus status, Money balance, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.iban = iban;
        this.status = status;
        this.balance = balance;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Account create(UUID ownerId, String iban, String currency) {
        Instant now = Instant.now();
        return new Account(UUID.randomUUID(), ownerId, iban, AccountStatus.ACTIVE, Money.zero(currency), now, now);
    }

    public boolean isActive() {
        return AccountStatus.ACTIVE.equals(status);
    }

    public UUID id() { return id; }
    public UUID ownerId() { return ownerId; }
    public String iban() { return iban; }
    public AccountStatus status() { return status; }
    public Money balance() { return balance; }
    public Instant createdAt() { return createdAt; }
    public Instant updatedAt() { return updatedAt; }
}
