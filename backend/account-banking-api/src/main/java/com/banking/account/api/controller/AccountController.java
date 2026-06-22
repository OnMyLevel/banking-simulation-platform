package com.banking.account.api.controller;

import com.banking.account.api.mapper.AccountApiMapper;
import com.banking.account.api.request.CreateAccountRequest;
import com.banking.account.api.response.AccountResponse;
import com.banking.account.api.response.AccountsResponse;
import com.banking.account.application.command.CreateAccountCommand;
import com.banking.account.application.facade.AccountFacade;
import com.banking.account.domain.model.Account;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountFacade accountFacade;

    public AccountController(AccountFacade accountFacade) {
        this.accountFacade = accountFacade;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount(@Valid @RequestBody CreateAccountRequest request) {
        Account account = accountFacade.createAccount(new CreateAccountCommand(request.ownerId(), request.currency()));
        return AccountApiMapper.toResponse(account);
    }

    @GetMapping("/{accountId}")
    public AccountResponse getAccount(@PathVariable UUID accountId) {
        return AccountApiMapper.toResponse(accountFacade.getAccount(accountId));
    }

    @GetMapping
    public AccountsResponse findAccountsByOwner(
        @RequestParam("owner_id") UUID ownerId,
        @RequestParam(defaultValue = "25") int limit,
        @RequestParam(defaultValue = "0") int offset
    ) {
        int safeLimit = Math.min(Math.max(limit, 1), 100);
        int safeOffset = Math.max(offset, 0);
        var items = accountFacade.findAccountsByOwner(ownerId, safeLimit, safeOffset)
            .stream()
            .map(AccountApiMapper::toResponse)
            .toList();
        return new AccountsResponse(items, safeLimit, safeOffset, safeOffset + safeLimit);
    }
}
