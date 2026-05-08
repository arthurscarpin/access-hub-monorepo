package com.arthurscarpin.acs.infrastructure.presentation.controller;

import com.arthurscarpin.acs.AccessControlSystemIntegrationTest;
import com.arthurscarpin.acs.infrastructure.persistence.entity.table.ScopeEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.table.UserEntity;
import com.arthurscarpin.acs.infrastructure.persistence.repository.table.ScopeRepository;
import com.arthurscarpin.acs.infrastructure.persistence.repository.table.UserRepository;
import com.arthurscarpin.acs.infrastructure.presentation.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class LoginControllerTest extends AccessControlSystemIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ScopeRepository scopeRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAllInBatch();
        scopeRepository.deleteAllInBatch();

        ScopeEntity scopeEntity = new ScopeEntity(
                null,
                "SCOPE_admin:all"
        );

        ScopeEntity savedScope = scopeRepository.save(scopeEntity);

        UserEntity userEntity = new UserEntity(
                null,
                "admin",
                "admin@admin.com",
                passwordEncoder.encode("Password@123"),
                true,
                List.of(savedScope)
        );

        userRepository.save(userEntity);
    }

    @Test
    @DisplayName("Given valid credentials When logging in Then returns 200 OK with token")
    void shouldLoginSuccessfully() throws Exception {

        LoginRequest request = new LoginRequest(
                "admin@admin.com",
                "Password@123"
        );

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.expiresIn").isNumber());
    }

    @Test
    @DisplayName("Given invalid login request When logging in Then returns 400 Bad Request")
    void shouldReturnBadRequestWhenInvalidData() throws Exception {

        LoginRequest request = new LoginRequest(
                "invalid-email",
                ""
        );

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Given invalid credentials When logging in Then returns 401 Unauthorized")
    void shouldReturnUnauthorizedWhenInvalidCredentials() throws Exception {

        LoginRequest request = new LoginRequest(
                "admin@admin.com",
                "WrongPassword"
        );

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}