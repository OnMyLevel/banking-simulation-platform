package com.banking.account.domain.repository;

import com.banking.account.domain.model.Account;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Account persist(Account account);
    Optional<Account> findById(UUID accountId);
}
