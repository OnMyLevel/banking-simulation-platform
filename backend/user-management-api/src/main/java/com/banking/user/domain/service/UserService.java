package com.banking.user.domain.service;

import com.banking.user.domain.exception.EmailAlreadyUsedException;
import com.banking.user.domain.exception.UserNotFoundException;
import com.banking.user.domain.model.User;
import com.banking.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createClient(String firstname, String lastname, String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyUsedException(email);
        }
        User user = User.createClient(firstname, lastname, email);
        return userRepository.save(user);
    }

    public User getUser(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
