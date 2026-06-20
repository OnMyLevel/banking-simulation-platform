package com.banking.account.domain.service;

import com.banking.account.domain.model.Account;
import com.banking.account.domain.repository.AccountRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountServiceTest {
    private final AccountRepository repository = mock(AccountRepository.class);
    private final AccountService service = new AccountService(repository);

    @Test
    void shouldCreateAccount() {
        when(repository.persist(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account account = service.createAccount(UUID.randomUUID(), "EUR");

        assertThat(account.id()).isNotNull();
        assertThat(account.balance().amount()).isZero();
    }
}
