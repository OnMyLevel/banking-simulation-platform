package com.banking.account.domain.service;

import com.banking.account.domain.exception.AccountClosedException;
import com.banking.account.domain.exception.AccountNotActiveException;
import com.banking.account.domain.exception.AccountNotFoundException;
import com.banking.account.domain.model.Account;
import com.banking.account.domain.repository.AccountRepository;

import java.util.List;
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

    public Account getAccount(UUID accountId) {
        return accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    public Account requireActiveAccount(UUID accountId) {
        Account account = getAccount(accountId);
        if (account.isClosed()) {
            throw new AccountClosedException(accountId);
        }
        if (!account.isActive()) {
            throw new AccountNotActiveException(accountId);
        }
        return account;
    }

    public List<Account> findAccountsByOwner(UUID ownerId, int limit, int offset) {
        return accountRepository.findByOwnerId(ownerId, limit, offset);
    }
}
