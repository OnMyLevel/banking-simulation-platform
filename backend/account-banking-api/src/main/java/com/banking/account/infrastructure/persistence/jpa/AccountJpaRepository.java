package com.banking.account.infrastructure.persistence.jpa;

import com.banking.account.infrastructure.persistence.entity.AccountEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, UUID> {
    List<AccountEntity> findByOwnerId(UUID ownerId, Pageable pageable);
}
