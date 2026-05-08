package com.arthurscarpin.acs.infrastructure.presentation.controller;

import com.arthurscarpin.acs.AccessControlSystemIntegrationTest;
import com.arthurscarpin.acs.infrastructure.persistence.entity.table.ScopeEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.table.UserEntity;
import com.arthurscarpin.acs.infrastructure.persistence.repository.table.ScopeRepository;
import com.arthurscarpin.acs.infrastructure.persistence.repository.table.UserRepository;
import com.arthurscarpin.acs.infrastructure.presentation.request.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UserControllerTest extends AccessControlSystemIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ScopeRepository scopeRepository;

    @Autowired
    private UserRepository userRepository;

    private UUID scopeId;

    @BeforeEach
    void setup() {
        userRepository.deleteAllInBatch();
        scopeRepository.deleteAllInBatch();

        ScopeEntity scopeEntity = new ScopeEntity(
                null,
                "SCOPE_admin:all"
        );

        ScopeEntity savedScope = scopeRepository.save(scopeEntity);
        scopeId = savedScope.getId();

        UserEntity userEntity = new UserEntity(
                null,
                "admin",
                "admin@admin.com",
                "Password@123",
                true,
                List.of(savedScope)
        );
        userRepository.save(userEntity);
    }

    @Test
    @DisplayName("Given valid user request When saving Then returns 201 Created with user data")
    @WithMockUser(authorities = {"SCOPE_admin:all"})
    void shouldCreateUserSuccessfully() throws Exception {

        UserRequest request = new UserRequest(
                "Ana Santos",
                "ana.santos@email.com",
                "Password@123",
                List.of(scopeId)
        );

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.email").value(request.email()))
                .andExpect(jsonPath("$.scopes").isArray())
                .andExpect(jsonPath("$.scopes[0]").value(scopeId.toString()));
    }

    @Test
    @DisplayName("Given invalid user request When saving Then returns 400 Bad Request")
    @WithMockUser(authorities = {"SCOPE_admin:all"})
    void shouldReturnBadRequestWhenInvalidData() throws Exception {

        UserRequest request = new UserRequest(
                "",
                "invalid-email",
                "123",
                List.of()
        );

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Given non existing scope When saving user Then returns 404 Not found")
    @WithMockUser(authorities = {"SCOPE_admin:all"})
    void shouldReturnBadRequestWhenScopeDoesNotExist() throws Exception {

        UserRequest request = new UserRequest(
                "Ana Santos",
                "ana.santos@email.com",
                "Password@123",
                List.of(UUID.randomUUID())
        );

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}