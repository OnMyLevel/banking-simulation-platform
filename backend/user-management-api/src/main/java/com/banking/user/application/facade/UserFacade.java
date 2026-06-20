package com.banking.user.application.facade;

import com.banking.user.application.command.CreateUserCommand;
import com.banking.user.domain.model.User;
import com.banking.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserFacade {
    private final UserRepository userRepository;

    public UserFacade(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(CreateUserCommand command) {
        User user = User.createClient(command.firstname(), command.lastname(), command.email());
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUser(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
