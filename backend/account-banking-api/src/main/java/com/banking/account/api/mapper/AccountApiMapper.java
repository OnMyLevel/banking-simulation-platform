package com.banking.account.api.mapper;

import com.banking.account.api.response.AccountResponse;
import com.banking.account.api.response.MoneyResponse;
import com.banking.account.domain.model.Account;

public final class AccountApiMapper {
    private AccountApiMapper() {
    }

    public static AccountResponse toResponse(Account account) {
        return new AccountResponse(
            account.id(),
            account.ownerId(),
            account.iban(),
            account.status().name(),
            new MoneyResponse(account.balance().amount().toPlainString(), account.balance().currency()),
            account.createdAt(),
            account.updatedAt()
        );
    }
}
