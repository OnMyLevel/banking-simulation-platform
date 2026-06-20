package com.banking.user.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void shouldCreateActiveClientUser() {
        User user = User.createClient("Meril", "Banzouzi", "meril@example.com");

        assertThat(user.id()).isNotNull();
        assertThat(user.firstname()).isEqualTo("Meril");
        assertThat(user.lastname()).isEqualTo("Banzouzi");
        assertThat(user.email()).isEqualTo("meril@example.com");
        assertThat(user.status()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.roles()).containsExactly(RoleName.ROLE_CLIENT);
        assertThat(user.createdAt()).isNotNull();
        assertThat(user.updatedAt()).isNotNull();
    }
}
