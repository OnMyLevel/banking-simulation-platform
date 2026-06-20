package com.banking.account.domain.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    @Test
    void shouldCreateActiveAccountWithZeroBalance() {
        UUID ownerId = UUID.randomUUID();

        Account account = Account.create(ownerId, "FR7612345678901234567890185", "EUR");

        assertThat(account.id()).isNotNull();
        assertThat(account.ownerId()).isEqualTo(ownerId);
        assertThat(account.iban()).isEqualTo("FR7612345678901234567890185");
        assertThat(account.status()).isEqualTo(AccountStatus.ACTIVE);
        assertThat(account.balance().amount()).isZero();
        assertThat(account.balance().currency()).isEqualTo("EUR");
        assertThat(account.isActive()).isTrue();
    }
}
