package com.arthurscarpin.acs.infrastructure.presentation.controller;

import com.arthurscarpin.acs.AccessControlSystemIntegrationTest;
import com.arthurscarpin.acs.core.accessevent.domain.AccessResult;
import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import com.arthurscarpin.acs.infrastructure.persistence.entity.AccessEventEntity;
import com.arthurscarpin.acs.infrastructure.persistence.repository.AccessEventRepository;
import com.arthurscarpin.acs.infrastructure.presentation.request.AccessEventRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.OffsetDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccessEventControllerTest extends AccessControlSystemIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccessEventRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAllInBatch();

        AccessEventEntity entity = AccessEventEntity.builder()
                .plate("BRA1S28")
                .timestamp(OffsetDateTime.now())
                .direction(Direction.IN)
                .result(AccessResult.AUTHORIZED)
                .build();

        repository.save(entity);
    }

    @Test
    @DisplayName("Given valid access event request When saving Then returns 201 Created with event data")
    @WithMockUser(authorities = {"SCOPE_admin:all", "SCOPE_access_event:write"})
    void shouldCreateAccessEventSuccessfully() throws Exception {

        AccessEventRequest request = AccessEventRequest.builder()
                .plate("BRA1S28")
                .direction(Direction.IN)
                .timestamp(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .build();

        mockMvc.perform(post("/access-events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.plate").value(request.plate()))
                .andExpect(jsonPath("$.direction").value(request.direction().name()))
                .andExpect(jsonPath("$.timestamp").value(request.timestamp().toInstant().toString()))
                .andExpect(jsonPath("$.result").isNotEmpty());
    }

    @Test
    @DisplayName("Given valid request When access is denied Then returns 201 Created with DENIED result")
    @WithMockUser(authorities = {"SCOPE_admin:all", "SCOPE_access_event:write"})
    void shouldReturnCreatedWithDeniedResultWhenAccessIsDenied() throws Exception {

        AccessEventRequest request = AccessEventRequest.builder()
                .plate("BRA1S28")
                .direction(Direction.IN)
                .timestamp(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .build();

        mockMvc.perform(post("/access-events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.plate").value(request.plate()))
                .andExpect(jsonPath("$.direction").value(request.direction().name()))
                .andExpect(jsonPath("$.timestamp").value(request.timestamp().toInstant().toString()))
                .andExpect(jsonPath("$.result").value("DENIED"));
    }

    @Test
    @DisplayName("Given invalid request data When saving Then returns 400 Bad Request")
    @WithMockUser(authorities = {"SCOPE_admin:all", "SCOPE_access_event:write"})
    void shouldReturnBadRequestWhenRequestIsInvalid() throws Exception {

        AccessEventRequest invalidRequest = AccessEventRequest.builder()
                .plate("")
                .direction(null)
                .timestamp(null)
                .build();

        mockMvc.perform(post("/access-events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Given unauthenticated user When saving Then returns 401 Unauthorized")
    void shouldReturnUnauthorizedWhenUserIsNotAuthenticated() throws Exception {

        AccessEventRequest request = AccessEventRequest.builder()
                .plate("BRA1S28")
                .direction(Direction.IN)
                .timestamp(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .build();

        mockMvc.perform(post("/access-events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Given authenticated user without permission When saving Then returns 403 Forbidden")
    @WithMockUser(authorities = {"SCOPE_other_scope"})
    void shouldReturnForbiddenWhenUserHasNoPermission() throws Exception {

        AccessEventRequest request = AccessEventRequest.builder()
                .plate("BRA1S28")
                .direction(Direction.IN)
                .timestamp(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .build();

        mockMvc.perform(post("/access-events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Given valid request When resource not found Then returns 404 Not Found")
    @WithMockUser(authorities = {"SCOPE_admin:all", "SCOPE_access_event:write"})
    void shouldReturnNotFoundWhenResourceDoesNotExist() throws Exception {

        mockMvc.perform(post("/access-events/non-existent")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}