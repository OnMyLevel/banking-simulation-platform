package com.banking.core.api.controller;

import com.banking.core.application.facade.OutboxOpsFacade;
import com.banking.core.config.InternalSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OutboxInternalController.class)
@Import(InternalSecurityConfiguration.class)
class OutboxInternalControllerSecurityTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OutboxOpsFacade facade;

    @Test
    void shouldRejectInternalListWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/internal/outbox-events"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowInternalListForOpsRole() throws Exception {
        when(facade.findByStatus(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.anyInt()))
            .thenReturn(List.of());

        mockMvc.perform(get("/internal/outbox-events").with(user("ops").roles("OPS")))
            .andExpect(status().isOk());
    }

    @Test
    void shouldRejectInternalRetryForUserWithoutOpsRole() throws Exception {
        mockMvc.perform(post("/internal/outbox-events/00000000-0000-0000-0000-000000000001/retry")
                .with(user("customer").roles("USER")))
            .andExpect(status().isForbidden());
    }
}
