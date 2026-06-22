package com.banking.core.application.facade;

import com.banking.core.domain.model.Money;
import com.banking.core.domain.model.Operation;
import com.banking.core.domain.port.AccountClient;
import com.banking.core.domain.port.AuditPublisher;
import com.banking.core.domain.repository.OperationRepository;
import com.banking.core.domain.service.CoreBankingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CoreBankingFacade {
    private final CoreBankingService coreBankingService;

    public CoreBankingFacade(OperationRepository operationRepository, AccountClient accountClient, AuditPublisher auditPublisher) {
        this.coreBankingService = new CoreBankingService(operationRepository, accountClient, auditPublisher);
    }

    @Transactional
    public Operation credit(UUID accountId, Money money, String idempotencyKey) {
        return coreBankingService.credit(accountId, money, idempotencyKey);
    }

    @Transactional
    public Operation debit(UUID accountId, Money money, String idempotencyKey) {
        return coreBankingService.debit(accountId, money, idempotencyKey);
    }

    @Transactional
    public Operation transfer(UUID sourceAccountId, UUID targetAccountId, Money money, String idempotencyKey) {
        return coreBankingService.transfer(sourceAccountId, targetAccountId, money, idempotencyKey);
    }

    @Transactional(readOnly = true)
    public List<Operation> history(UUID accountId, int limit, int offset) {
        return coreBankingService.history(accountId, limit, offset);
    }
}
