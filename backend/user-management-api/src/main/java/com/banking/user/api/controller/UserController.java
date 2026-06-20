package com.banking.user.api.controller;

import com.banking.user.api.mapper.UserApiMapper;
import com.banking.user.api.request.CreateUserRequest;
import com.banking.user.api.response.UserResponse;
import com.banking.user.application.command.CreateUserCommand;
import com.banking.user.application.facade.UserFacade;
import com.banking.user.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserFacade userFacade;

    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = userFacade.createUser(new CreateUserCommand(
            request.firstname(),
            request.lastname(),
            request.email()
        ));
        return UserApiMapper.toResponse(user);
    }

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable UUID userId) {
        User user = userFacade.getUser(userId);
        return UserApiMapper.toResponse(user);
    }
}
