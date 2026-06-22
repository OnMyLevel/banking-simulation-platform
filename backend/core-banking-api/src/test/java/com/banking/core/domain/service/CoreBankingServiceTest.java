package com.banking.core.domain.service;

import com.banking.core.domain.exception.IdempotencyKeyRequiredException;
import com.banking.core.domain.model.Money;
import com.banking.core.domain.model.Operation;
import com.banking.core.domain.model.OperationKind;
import com.banking.core.domain.repository.OperationRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CoreBankingServiceTest {
    private final OperationRepository repository = mock(OperationRepository.class);
    private final CoreBankingService service = new CoreBankingService(repository);

    @Test
    void shouldCreateCreditOperation() {
        when(repository.findByIdempotencyKey("key-1")).thenReturn(Optional.empty());
        when(repository.persist(any(Operation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Operation operation = service.credit(UUID.randomUUID(), Money.of(BigDecimal.TEN, "EUR"), "key-1");

        assertThat(operation.kind()).isEqualTo(OperationKind.CREDIT);
        assertThat(operation.money().amount()).isEqualByComparingTo(BigDecimal.TEN);
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
