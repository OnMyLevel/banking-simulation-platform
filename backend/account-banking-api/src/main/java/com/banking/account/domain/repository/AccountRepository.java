package com.banking.account.domain.repository;

import com.banking.account.domain.model.Account;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Account persist(Account account);
    Optional<Account> findById(UUID accountId);
    List<Account> findByOwnerId(UUID ownerId, int limit, int offset);
}
