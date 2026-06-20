package com.banking.user.infrastructure.persistence.adapter;

import com.banking.user.domain.model.RoleName;
import com.banking.user.domain.model.User;
import com.banking.user.domain.model.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@Import(UserJpaAdapter.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserJpaAdapterIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
        .withDatabaseName("banking_test")
        .withUsername("banking")
        .withPassword("banking");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> true);
        registry.add("spring.flyway.schemas", () -> "user_schema");
        registry.add("spring.flyway.default-schema", () -> "user_schema");
        registry.add("spring.jpa.properties.hibernate.default_schema", () -> "user_schema");
    }

    @Autowired
    private UserJpaAdapter userJpaAdapter;

    @Test
    void shouldSaveAndFindUser() {
        UUID userId = UUID.randomUUID();
        Instant now = Instant.now();
        User user = new User(userId, "Meril", "Banzouzi", "meril@example.com", UserStatus.ACTIVE, Set.of(RoleName.ROLE_CLIENT), now, now);

        userJpaAdapter.save(user);
        Optional<User> result = userJpaAdapter.findById(userId);

        assertThat(result).isPresent();
        assertThat(result.get().email()).isEqualTo("meril@example.com");
        assertThat(userJpaAdapter.existsByEmail("meril@example.com")).isTrue();
    }
}
