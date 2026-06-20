package com.banking.user.domain.repository;

import com.banking.user.domain.model.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);

    Optional<User> findById(UUID id);

    boolean existsByEmail(String email);
}
