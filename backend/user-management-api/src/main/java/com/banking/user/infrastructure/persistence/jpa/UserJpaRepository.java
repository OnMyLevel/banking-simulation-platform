package com.banking.user.infrastructure.persistence.jpa;

import com.banking.user.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    @Query("select count(u) > 0 from UserEntity u where u.email = :email")
    boolean hasEmail(String email);
}
