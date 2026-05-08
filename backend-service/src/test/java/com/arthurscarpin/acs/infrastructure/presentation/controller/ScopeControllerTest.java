package com.arthurscarpin.acs.infrastructure.presentation.controller;

import com.arthurscarpin.acs.AccessControlSystemIntegrationTest;
import com.arthurscarpin.acs.infrastructure.persistence.entity.table.ScopeEntity;
import com.arthurscarpin.acs.infrastructure.persistence.repository.table.ScopeRepository;
import com.arthurscarpin.acs.infrastructure.persistence.repository.table.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ScopeControllerTest extends AccessControlSystemIntegrationTest {

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
        ScopeEntity scope1 = new ScopeEntity(null, "admin:all");
        ScopeEntity scope2 = new ScopeEntity(null, "user:read");
        ScopeEntity scope3 = new ScopeEntity(null, "owner:write");

        ScopeEntity savedScope1 = scopeRepository.save(scope1);
        scopeRepository.save(scope2);
        scopeRepository.save(scope3);
        scopeId = savedScope1.getId();
    }

    @Test
    @DisplayName("Given existing scopes When retrieving all scopes Then returns 200 OK with scope list")
    void shouldReturnAllScopesSuccessfully() throws Exception {
        mockMvc.perform(get("/scopes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].name").value("admin:all"))
                .andExpect(jsonPath("$[1].name").value("user:read"))
                .andExpect(jsonPath("$[2].name").value("owner:write"));
    }

    @Test
    @DisplayName("Given no scopes exist When retrieving all scopes Then returns 200 OK with empty list")
    void shouldReturnEmptyListWhenNoScopesExist() throws Exception {
        scopeRepository.deleteAllInBatch();

        mockMvc.perform(get("/scopes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Given scopes exist When retrieving all scopes Then returns scopes with correct structure")
    void shouldReturnScopesWithCorrectStructure() throws Exception {
        mockMvc.perform(get("/scopes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").exists())
                .andExpect(jsonPath("$[*].name").exists())
                .andExpect(jsonPath("$[0].id").value(scopeId.toString()))
                .andExpect(jsonPath("$[0].name").value("admin:all"));
    }
}