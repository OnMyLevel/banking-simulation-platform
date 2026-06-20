package com.banking.account.domain.service;

import com.banking.account.domain.model.Account;
import com.banking.account.domain.repository.AccountRepository;

import java.util.UUID;

public class AccountService {
    private final AccountRepository accountRepository;
    private final IbanGenerator ibanGenerator = new IbanGenerator();

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(UUID ownerId, String currency) {
        Account account = Account.create(ownerId, ibanGenerator.generate(), currency);
        return accountRepository.persist(account);
    }
}
