package com.banking.account.domain.service;

import com.banking.account.domain.exception.AccountClosedException;
import com.banking.account.domain.exception.AccountNotActiveException;
import com.banking.account.domain.model.Account;
import com.banking.account.domain.model.AccountStatus;
import com.banking.account.domain.model.Money;
import com.banking.account.domain.repository.AccountRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Test
    void shouldReturnActiveAccount() {
        UUID accountId = UUID.randomUUID();
        Account account = account(accountId, AccountStatus.ACTIVE);
        when(repository.findById(accountId)).thenReturn(Optional.of(account));

        Account result = service.requireActiveAccount(accountId);

        assertThat(result.id()).isEqualTo(accountId);
    }

    @Test
    void shouldRejectBlockedAccount() {
        UUID accountId = UUID.randomUUID();
        when(repository.findById(accountId)).thenReturn(Optional.of(account(accountId, AccountStatus.BLOCKED)));

        assertThatThrownBy(() -> service.requireActiveAccount(accountId))
            .isInstanceOf(AccountNotActiveException.class);
    }

    @Test
    void shouldRejectClosedAccount() {
        UUID accountId = UUID.randomUUID();
        when(repository.findById(accountId)).thenReturn(Optional.of(account(accountId, AccountStatus.CLOSED)));

        assertThatThrownBy(() -> service.requireActiveAccount(accountId))
            .isInstanceOf(AccountClosedException.class);
    }

    private Account account(UUID accountId, AccountStatus status) {
        Instant now = Instant.now();
        return new Account(accountId, UUID.randomUUID(), "FR7612345678901234567890185", status, new Money(BigDecimal.ZERO, "EUR"), now, now);
    }
}
