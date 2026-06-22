package com.banking.core.domain.service;

import com.banking.core.domain.exception.IdempotencyKeyRequiredException;
import com.banking.core.domain.exception.InsufficientFundsException;
import com.banking.core.domain.model.Money;
import com.banking.core.domain.model.Operation;
import com.banking.core.domain.repository.OperationRepository;

import java.util.List;
import java.util.UUID;

public class CoreBankingService {
    private final OperationRepository operationRepository;

    public CoreBankingService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public Operation credit(UUID accountId, Money money, String idempotencyKey) {
        validateIdempotencyKey(idempotencyKey);
        return operationRepository.findByIdempotencyKey(idempotencyKey)
            .orElseGet(() -> operationRepository.persist(Operation.credit(accountId, money, idempotencyKey)));
    }

    public Operation debit(UUID accountId, Money money, String idempotencyKey) {
        validateIdempotencyKey(idempotencyKey);
        return operationRepository.findByIdempotencyKey(idempotencyKey)
            .orElseGet(() -> {
                ensureEnoughFunds(accountId, money);
                return operationRepository.persist(Operation.debit(accountId, money, idempotencyKey));
            });
    }

    public Operation transfer(UUID sourceAccountId, UUID targetAccountId, Money money, String idempotencyKey) {
        validateIdempotencyKey(idempotencyKey);
        return operationRepository.findByIdempotencyKey(idempotencyKey)
            .orElseGet(() -> {
                ensureEnoughFunds(sourceAccountId, money);
                return operationRepository.persist(Operation.transfer(sourceAccountId, targetAccountId, money, idempotencyKey));
            });
    }

    public List<Operation> history(UUID accountId, int limit, int offset) {
        return operationRepository.findByAccountId(accountId, limit, offset);
    }

    private void validateIdempotencyKey(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new IdempotencyKeyRequiredException();
        }
    }

    private void ensureEnoughFunds(UUID accountId, Money money) {
        if (operationRepository.balanceOf(accountId).compareTo(money.amount()) < 0) {
            throw new InsufficientFundsException(accountId);
        }
    }
}
