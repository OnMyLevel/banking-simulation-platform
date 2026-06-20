package com.banking.account.application.facade;

import com.banking.account.application.command.CreateAccountCommand;
import com.banking.account.domain.model.Account;
import com.banking.account.domain.repository.AccountRepository;
import com.banking.account.domain.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AccountFacade {
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    public AccountFacade(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.accountService = new AccountService(accountRepository);
    }

    @Transactional
    public Account createAccount(CreateAccountCommand command) {
        return accountService.createAccount(command.ownerId(), command.currency());
    }

    @Transactional(readOnly = true)
    public Account getAccount(UUID accountId) {
        return accountRepository.findById(accountId).orElseThrow();
    }
}
