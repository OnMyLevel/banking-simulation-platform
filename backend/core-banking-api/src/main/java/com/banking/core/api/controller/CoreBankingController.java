package com.banking.core.api.controller;

import com.banking.core.api.mapper.CoreApiMapper;
import com.banking.core.api.request.CreditRequest;
import com.banking.core.api.request.DebitRequest;
import com.banking.core.api.request.TransferRequest;
import com.banking.core.api.response.OperationResponse;
import com.banking.core.application.facade.CoreBankingFacade;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/operations")
public class CoreBankingController {
    private final CoreBankingFacade coreBankingFacade;

    public CoreBankingController(CoreBankingFacade coreBankingFacade) {
        this.coreBankingFacade = coreBankingFacade;
    }

    @PostMapping("/credits")
    @ResponseStatus(HttpStatus.CREATED)
    public OperationResponse credit(@Valid @RequestBody CreditRequest request, @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return CoreApiMapper.toResponse(coreBankingFacade.credit(request.accountId(), CoreApiMapper.toMoney(request.money()), idempotencyKey));
    }

    @PostMapping("/debits")
    @ResponseStatus(HttpStatus.CREATED)
    public OperationResponse debit(@Valid @RequestBody DebitRequest request, @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return CoreApiMapper.toResponse(coreBankingFacade.debit(request.accountId(), CoreApiMapper.toMoney(request.money()), idempotencyKey));
    }

    @PostMapping("/transfers")
    @ResponseStatus(HttpStatus.CREATED)
    public OperationResponse transfer(@Valid @RequestBody TransferRequest request, @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return CoreApiMapper.toResponse(coreBankingFacade.transfer(request.sourceAccountId(), request.targetAccountId(), CoreApiMapper.toMoney(request.money()), idempotencyKey));
    }

    @GetMapping("/accounts/{accountId}")
    public List<OperationResponse> history(@PathVariable UUID accountId) {
        return coreBankingFacade.history(accountId, 25, 0).stream().map(CoreApiMapper::toResponse).toList();
    }
}
