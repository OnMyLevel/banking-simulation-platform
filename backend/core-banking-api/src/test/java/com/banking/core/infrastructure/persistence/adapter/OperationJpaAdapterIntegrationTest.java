package com.banking.core.infrastructure.persistence.adapter;

import com.banking.core.domain.model.Money;
import com.banking.core.domain.model.Operation;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@Import(OperationJpaAdapter.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OperationJpaAdapterIntegrationTest {

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
        registry.add("spring.flyway.schemas", () -> "core_schema");
        registry.add("spring.flyway.default-schema", () -> "core_schema");
        registry.add("spring.jpa.properties.hibernate.default_schema", () -> "core_schema");
    }

    @Autowired
    private OperationJpaAdapter operationJpaAdapter;

    @Test
    void shouldPersistAndFindOperationByIdempotencyKey() {
        UUID accountId = UUID.randomUUID();
        Operation operation = Operation.credit(accountId, Money.of(BigDecimal.TEN, "EUR"), "key-it-1");

        operationJpaAdapter.persist(operation);
        Optional<Operation> result = operationJpaAdapter.findByIdempotencyKey("key-it-1");

        assertThat(result).isPresent();
        assertThat(result.get().targetAccountId()).isEqualTo(accountId);
    }

    @Test
    void shouldCalculateBalanceFromOperations() {
        UUID accountId = UUID.randomUUID();
        operationJpaAdapter.persist(Operation.credit(accountId, Money.of(BigDecimal.TEN, "EUR"), "key-it-2"));
        operationJpaAdapter.persist(Operation.debit(accountId, Money.of(BigDecimal.ONE, "EUR"), "key-it-3"));

        BigDecimal balance = operationJpaAdapter.balanceOf(accountId);
        List<Operation> history = operationJpaAdapter.findByAccountId(accountId, 25, 0);

        assertThat(balance).isEqualByComparingTo("9.00");
        assertThat(history).hasSize(2);
    }
}
