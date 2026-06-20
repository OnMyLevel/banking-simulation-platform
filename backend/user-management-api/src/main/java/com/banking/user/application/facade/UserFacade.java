package com.banking.user.application.facade;

import com.banking.user.application.command.CreateUserCommand;
import com.banking.user.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserFacade {

    public User createUser(CreateUserCommand command) {
        return User.createClient(command.firstname(), command.lastname(), command.email());
    }

    public User getUser(UUID userId) {
        return null;
    }
}
