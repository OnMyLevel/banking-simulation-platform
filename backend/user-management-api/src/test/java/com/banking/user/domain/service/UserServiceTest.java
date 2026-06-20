package com.banking.user.domain.service;

import com.banking.user.domain.exception.EmailAlreadyUsedException;
import com.banking.user.domain.exception.UserNotFoundException;
import com.banking.user.domain.model.RoleName;
import com.banking.user.domain.model.User;
import com.banking.user.domain.model.UserStatus;
import com.banking.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = new UserService(userRepository);

    @Test
    void shouldCreateClientWhenEmailIsAvailable() {
        when(userRepository.existsByEmail("meril@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.createClient("Meril", "Banzouzi", "meril@example.com");

        assertThat(user.email()).isEqualTo("meril@example.com");
        assertThat(user.status()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.roles()).containsExactly(RoleName.ROLE_CLIENT);
    }

    @Test
    void shouldRejectClientCreationWhenEmailIsAlreadyUsed() {
        when(userRepository.existsByEmail("meril@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createClient("Meril", "Banzouzi", "meril@example.com"))
            .isInstanceOf(EmailAlreadyUsedException.class)
            .hasMessageContaining("Email already used");
    }

    @Test
    void shouldReturnUserWhenFound() {
        UUID userId = UUID.randomUUID();
        Instant now = Instant.now();
        User expected = new User(userId, "Meril", "Banzouzi", "meril@example.com", UserStatus.ACTIVE, Set.of(RoleName.ROLE_CLIENT), now, now);
        when(userRepository.findById(userId)).thenReturn(Optional.of(expected));

        User user = userService.getUser(userId);

        assertThat(user.id()).isEqualTo(userId);
    }

    @Test
    void shouldThrowWhenUserIsNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUser(userId))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessageContaining("User not found");
    }
}
