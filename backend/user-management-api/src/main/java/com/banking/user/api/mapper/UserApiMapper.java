package com.banking.user.api.mapper;

import com.banking.user.api.response.UserResponse;
import com.banking.user.domain.model.RoleName;
import com.banking.user.domain.model.User;

import java.util.stream.Collectors;

public final class UserApiMapper {
    private UserApiMapper() {
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
            user.id(),
            user.firstname(),
            user.lastname(),
            user.email(),
            user.status().name(),
            user.roles().stream().map(RoleName::name).collect(Collectors.toSet()),
            user.createdAt(),
            user.updatedAt()
        );
    }
}
