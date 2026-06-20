package com.banking.account.infrastructure.persistence.mapper;

import com.banking.account.domain.model.Account;
import com.banking.account.domain.model.AccountStatus;
import com.banking.account.domain.model.Money;
import com.banking.account.infrastructure.persistence.entity.AccountEntity;

public final class AccountMapper {
    private AccountMapper() {
    }

    public static AccountEntity toEntity(Account account) {
        AccountEntity entity = new AccountEntity();
        entity.id = account.id();
        entity.ownerId = account.ownerId();
        entity.iban = account.iban();
        entity.status = account.status().name();
        entity.balanceAmount = account.balance().amount();
        entity.currency = account.balance().currency();
        entity.createdAt = account.createdAt();
        entity.updatedAt = account.updatedAt();
        return entity;
    }

    public static Account toDomain(AccountEntity entity) {
        return new Account(
            entity.id,
            entity.ownerId,
            entity.iban,
            AccountStatus.valueOf(entity.status),
            new Money(entity.balanceAmount, entity.currency),
            entity.createdAt,
            entity.updatedAt
        );
    }
}
