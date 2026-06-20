package com.banking.account.api.controller;

import com.banking.account.application.facade.AccountFacade;
import com.banking.account.domain.model.Account;
import com.banking.account.domain.model.AccountStatus;
import com.banking.account.domain.model.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountFacade accountFacade;

    @Test
    void shouldCreateAccount() throws Exception {
        UUID ownerId = UUID.randomUUID();
        Instant now = Instant.now();
        Account account = new Account(UUID.randomUUID(), ownerId, "FR7612345678901234567890185", AccountStatus.ACTIVE, new Money(BigDecimal.ZERO, "EUR"), now, now);

        when(accountFacade.createAccount(any())).thenReturn(account);

        mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"ownerId\":\"" + ownerId + "\",\"currency\":\"EUR\"}"))
            .andExpect(status().isCreated());
    }
}
