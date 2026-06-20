package com.banking.user.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
    @NotBlank @Size(max = 100) String firstname,
    @NotBlank @Size(max = 100) String lastname,
    @Email @NotBlank @Size(max = 255) String email
) {}
