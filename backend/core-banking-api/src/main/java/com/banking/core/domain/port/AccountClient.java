package com.banking.core.domain.port;

import com.banking.core.domain.model.AccountSnapshot;

import java.util.UUID;

public interface AccountClient {
    AccountSnapshot getAccount(UUID accountId);
}
