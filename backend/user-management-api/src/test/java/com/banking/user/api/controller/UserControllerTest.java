package com.banking.user.api.controller;

import com.banking.user.application.facade.UserFacade;
import com.banking.user.domain.model.RoleName;
import com.banking.user.domain.model.User;
import com.banking.user.domain.model.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserFacade userFacade;

    @Test
    void shouldCreateUser() throws Exception {
        UUID userId = UUID.randomUUID();
        Instant now = Instant.now();
        User user = new User(userId, "Meril", "Banzouzi", "meril@example.com", UserStatus.ACTIVE, Set.of(RoleName.ROLE_CLIENT), now, now);

        when(userFacade.createUser(any())).thenReturn(user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstname\":\"Meril\",\"lastname\":\"Banzouzi\",\"email\":\"meril@example.com\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(userId.toString()))
            .andExpect(jsonPath("$.email").value("meril@example.com"));
    }
}
