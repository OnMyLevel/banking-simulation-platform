package com.banking.user.application.command;

public record CreateUserCommand(
    String firstname,
    String lastname,
    String email
) {}
