package com.banking.account.infrastructure.persistence.adapter;

import com.banking.account.domain.model.Account;
import com.banking.account.domain.repository.AccountRepository;
import com.banking.account.infrastructure.persistence.jpa.AccountJpaRepository;
import com.banking.account.infrastructure.persistence.mapper.AccountMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AccountJpaAdapter implements AccountRepository {
    private final AccountJpaRepository accountJpaRepository;

    public AccountJpaAdapter(AccountJpaRepository accountJpaRepository) {
        this.accountJpaRepository = accountJpaRepository;
    }

    @Override
    public Account persist(Account account) {
        return AccountMapper.toDomain(accountJpaRepository.save(AccountMapper.toEntity(account)));
    }

    @Override
    public Optional<Account> findById(UUID accountId) {
        return accountJpaRepository.findById(accountId).map(AccountMapper::toDomain);
    }

    @Override
    public List<Account> findByOwnerId(UUID ownerId, int limit, int offset) {
        int page = offset / limit;
        return accountJpaRepository.findByOwnerId(ownerId, PageRequest.of(page, limit))
            .stream()
            .map(AccountMapper::toDomain)
            .toList();
    }
}
