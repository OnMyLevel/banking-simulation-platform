package com.banking.core.api.controller;

import com.banking.core.application.facade.CoreBankingFacade;
import com.banking.core.domain.model.Money;
import com.banking.core.domain.model.Operation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CoreBankingController.class)
class CoreBankingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoreBankingFacade coreBankingFacade;

    @Test
    void shouldReturnPaginatedOperationHistory() throws Exception {
        UUID accountId = UUID.randomUUID();
        Operation operation = Operation.credit(accountId, Money.of(BigDecimal.TEN, "EUR"), "history-key-1");

        when(coreBankingFacade.history(accountId, 10, 20)).thenReturn(List.of(operation));

        mockMvc.perform(get("/operations/accounts/{accountId}", accountId)
                .param("limit", "10")
                .param("offset", "20"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.items[0].targetAccountId").value(accountId.toString()))
            .andExpect(jsonPath("$.limit").value(10))
            .andExpect(jsonPath("$.offset").value(20))
            .andExpect(jsonPath("$.nextOffset").value(30));
    }
}
