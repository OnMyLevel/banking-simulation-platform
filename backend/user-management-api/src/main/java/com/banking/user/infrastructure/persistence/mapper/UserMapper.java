package com.banking.user.infrastructure.persistence.mapper;

import com.banking.user.domain.model.RoleName;
import com.banking.user.domain.model.User;
import com.banking.user.domain.model.UserStatus;
import com.banking.user.infrastructure.persistence.entity.UserEntity;

public final class UserMapper {
    private UserMapper() {
    }

    public static UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.id());
        entity.setFirstname(user.firstname());
        entity.setLastname(user.lastname());
        entity.setEmail(user.email());
        entity.setStatus(user.status().name());
        entity.setRoles(user.roles());
        entity.setCreatedAt(user.createdAt());
        entity.setUpdatedAt(user.updatedAt());
        return entity;
    }

    public static User toDomain(UserEntity entity) {
        return new User(
            entity.getId(),
            entity.getFirstname(),
            entity.getLastname(),
            entity.getEmail(),
            UserStatus.valueOf(entity.getStatus()),
            entity.getRoles().stream().map(RoleName::valueOf).collect(java.util.stream.Collectors.toSet()),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
