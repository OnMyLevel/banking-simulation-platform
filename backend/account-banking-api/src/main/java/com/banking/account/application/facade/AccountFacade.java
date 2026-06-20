package com.banking.account.application.facade;

import com.banking.account.application.command.CreateAccountCommand;
import com.banking.account.domain.model.Account;
import com.banking.account.domain.repository.AccountRepository;
import com.banking.account.domain.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AccountFacade {
    private final AccountService accountService;

    public AccountFacade(AccountRepository accountRepository) {
        this.accountService = new AccountService(accountRepository);
    }

    @Transactional
    public Account createAccount(CreateAccountCommand command) {
        return accountService.createAccount(command.ownerId(), command.currency());
    }

    @Transactional(readOnly = true)
    public Account getAccount(UUID accountId) {
        return accountService.getAccount(accountId);
    }

    @Transactional(readOnly = true)
    public List<Account> findAccountsByOwner(UUID ownerId, int limit, int offset) {
        return accountService.findAccountsByOwner(ownerId, limit, offset);
    }
}
