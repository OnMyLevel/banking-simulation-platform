package com.banking.user.application.facade;

import com.banking.user.application.command.CreateUserCommand;
import com.banking.user.domain.model.User;
import com.banking.user.domain.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserFacade {
    private final UserService userService;

    public UserFacade(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    public User createUser(CreateUserCommand command) {
        return userService.createClient(command.firstname(), command.lastname(), command.email());
    }

    @Transactional(readOnly = true)
    public User getUser(UUID userId) {
        return userService.getUser(userId);
    }
}
