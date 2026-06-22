package com.banking.core.domain.service;

import com.banking.core.domain.exception.CoreAccountUnavailableException;
import com.banking.core.domain.exception.IdempotencyKeyRequiredException;
import com.banking.core.domain.exception.InsufficientFundsException;
import com.banking.core.domain.model.AccountSnapshot;
import com.banking.core.domain.model.Money;
import com.banking.core.domain.model.Operation;
import com.banking.core.domain.model.OperationKind;
import com.banking.core.domain.port.AccountClient;
import com.banking.core.domain.repository.OperationRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CoreBankingServiceTest {
    private final OperationRepository repository = mock(OperationRepository.class);
    private final AccountClient accountClient = mock(AccountClient.class);
    private final CoreBankingService service = new CoreBankingService(repository, accountClient);

    @Test
    void shouldCreateCreditOperation() {
        UUID accountId = UUID.randomUUID();
        when(accountClient.getAccount(accountId)).thenReturn(new AccountSnapshot(accountId, "ACTIVE"));
        when(repository.findByIdempotencyKey("key-1")).thenReturn(Optional.empty());
        when(repository.persist(any(Operation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Operation operation = service.credit(accountId, Money.of(BigDecimal.TEN, "EUR"), "key-1");

        assertThat(operation.kind()).isEqualTo(OperationKind.CREDIT);
        assertThat(operation.money().amount()).isEqualByComparingTo(BigDecimal.TEN);
        verify(repository).guardAccount(accountId);
    }

    @Test
    void shouldCreateDebitWhenFundsAreEnough() {
        UUID accountId = UUID.randomUUID();
        when(accountClient.getAccount(accountId)).thenReturn(new AccountSnapshot(accountId, "ACTIVE"));
        when(repository.findByIdempotencyKey("key-2")).thenReturn(Optional.empty());
        when(repository.balanceOf(accountId, "EUR")).thenReturn(BigDecimal.TEN);
        when(repository.persist(any(Operation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Operation operation = service.debit(accountId, Money.of(BigDecimal.ONE, "EUR"), "key-2");

        assertThat(operation.kind()).isEqualTo(OperationKind.DEBIT);
        verify(repository).guardAccount(accountId);
    }

    @Test
    void shouldRejectCreditWhenAccountIsNotActive() {
        UUID accountId = UUID.randomUUID();
        when(repository.findByIdempotencyKey("key-5")).thenReturn(Optional.empty());
        when(accountClient.getAccount(accountId)).thenReturn(new AccountSnapshot(accountId, "BLOCKED"));

        assertThatThrownBy(() -> service.credit(accountId, Money.of(BigDecimal.TEN, "EUR"), "key-5"))
            .isInstanceOf(CoreAccountUnavailableException.class);
    }

    @Test
    void shouldRejectDebitWhenFundsAreNotEnough() {
        UUID accountId = UUID.randomUUID();
        when(accountClient.getAccount(accountId)).thenReturn(new AccountSnapshot(accountId, "ACTIVE"));
        when(repository.findByIdempotencyKey("key-3")).thenReturn(Optional.empty());
        when(repository.balanceOf(accountId, "EUR")).thenReturn(BigDecimal.ZERO);

        assertThatThrownBy(() -> service.debit(accountId, Money.of(BigDecimal.TEN, "EUR"), "key-3"))
            .isInstanceOf(InsufficientFundsException.class);
    }

    @Test
    void shouldRejectTransferWhenTargetAccountIsNotActive() {
        UUID sourceAccountId = UUID.randomUUID();
        UUID targetAccountId = UUID.randomUUID();
        when(repository.findByIdempotencyKey("key-6")).thenReturn(Optional.empty());
        when(accountClient.getAccount(sourceAccountId)).thenReturn(new AccountSnapshot(sourceAccountId, "ACTIVE"));
        when(accountClient.getAccount(targetAccountId)).thenReturn(new AccountSnapshot(targetAccountId, "CLOSED"));

        assertThatThrownBy(() -> service.transfer(sourceAccountId, targetAccountId, Money.of(BigDecimal.TEN, "EUR"), "key-6"))
            .isInstanceOf(CoreAccountUnavailableException.class);
    }

    @Test
    void shouldRejectTransferWhenFundsAreNotEnough() {
        UUID sourceAccountId = UUID.randomUUID();
        UUID targetAccountId = UUID.randomUUID();
        when(repository.findByIdempotencyKey("key-4")).thenReturn(Optional.empty());
        when(accountClient.getAccount(sourceAccountId)).thenReturn(new AccountSnapshot(sourceAccountId, "ACTIVE"));
        when(accountClient.getAccount(targetAccountId)).thenReturn(new AccountSnapshot(targetAccountId, "ACTIVE"));
        when(repository.balanceOf(sourceAccountId, "EUR")).thenReturn(BigDecimal.ONE);

        assertThatThrownBy(() -> service.transfer(sourceAccountId, targetAccountId, Money.of(BigDecimal.TEN, "EUR"), "key-4"))
            .isInstanceOf(InsufficientFundsException.class);
    }

    @Test
    void shouldReturnExistingOperationForSameIdempotencyKey() {
        Operation existing = Operation.credit(UUID.randomUUID(), Money.of(BigDecimal.TEN, "EUR"), "key-1");
        when(repository.findByIdempotencyKey("key-1")).thenReturn(Optional.of(existing));

        Operation operation = service.credit(UUID.randomUUID(), Money.of(BigDecimal.ONE, "EUR"), "key-1");

        assertThat(operation.id()).isEqualTo(existing.id());
    }

    @Test
    void shouldRejectMissingIdempotencyKey() {
        assertThatThrownBy(() -> service.credit(UUID.randomUUID(), Money.of(BigDecimal.TEN, "EUR"), ""))
            .isInstanceOf(IdempotencyKeyRequiredException.class);
    }
}
