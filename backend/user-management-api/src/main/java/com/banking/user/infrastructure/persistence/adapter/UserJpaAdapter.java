package com.banking.user.infrastructure.persistence.adapter;

import com.banking.user.domain.model.User;
import com.banking.user.domain.repository.UserRepository;
import com.banking.user.infrastructure.persistence.jpa.UserJpaRepository;
import com.banking.user.infrastructure.persistence.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserJpaAdapter implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    public UserJpaAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save(User user) {
        return UserMapper.toDomain(userJpaRepository.save(UserMapper.toEntity(user)));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id).map(UserMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.hasEmail(email);
    }
}
