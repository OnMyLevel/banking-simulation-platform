package com.banking.core.domain.service;

import com.banking.core.domain.exception.CoreAccountUnavailableException;
import com.banking.core.domain.exception.IdempotencyKeyRequiredException;
import com.banking.core.domain.exception.InsufficientFundsException;
import com.banking.core.domain.model.AccountSnapshot;
import com.banking.core.domain.model.Money;
import com.banking.core.domain.model.Operation;
import com.banking.core.domain.port.AccountClient;
import com.banking.core.domain.repository.OperationRepository;

import java.util.List;
import java.util.UUID;

public class CoreBankingService {
    private final OperationRepository operationRepository;
    private final AccountClient accountClient;

    public CoreBankingService(OperationRepository operationRepository, AccountClient accountClient) {
        this.operationRepository = operationRepository;
        this.accountClient = accountClient;
    }

    public Operation credit(UUID accountId, Money money, String idempotencyKey) {
        validateIdempotencyKey(idempotencyKey);
        return operationRepository.findByIdempotencyKey(idempotencyKey)
            .orElseGet(() -> {
                checkAccount(accountId);
                operationRepository.guardAccount(accountId);
                return operationRepository.persist(Operation.credit(accountId, money, idempotencyKey));
            });
    }

    public Operation debit(UUID accountId, Money money, String idempotencyKey) {
        validateIdempotencyKey(idempotencyKey);
        return operationRepository.findByIdempotencyKey(idempotencyKey)
            .orElseGet(() -> {
                checkAccount(accountId);
                operationRepository.guardAccount(accountId);
                ensureEnoughFunds(accountId, money);
                return operationRepository.persist(Operation.debit(accountId, money, idempotencyKey));
            });
    }

    public Operation transfer(UUID sourceAccountId, UUID targetAccountId, Money money, String idempotencyKey) {
        validateIdempotencyKey(idempotencyKey);
        return operationRepository.findByIdempotencyKey(idempotencyKey)
            .orElseGet(() -> {
                checkAccount(sourceAccountId);
                checkAccount(targetAccountId);
                guardTransferAccounts(sourceAccountId, targetAccountId);
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

    private void checkAccount(UUID accountId) {
        AccountSnapshot account = accountClient.getAccount(accountId);
        if (!account.isActive()) {
            throw new CoreAccountUnavailableException(accountId);
        }
    }

    private void ensureEnoughFunds(UUID accountId, Money money) {
        if (operationRepository.balanceOf(accountId, money.currency()).compareTo(money.amount()) < 0) {
            throw new InsufficientFundsException(accountId);
        }
    }

    private void guardTransferAccounts(UUID sourceAccountId, UUID targetAccountId) {
        if (sourceAccountId.compareTo(targetAccountId) <= 0) {
            operationRepository.guardAccount(sourceAccountId);
            operationRepository.guardAccount(targetAccountId);
        } else {
            operationRepository.guardAccount(targetAccountId);
            operationRepository.guardAccount(sourceAccountId);
        }
    }
}
