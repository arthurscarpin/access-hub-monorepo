package com.arthurscarpin.acs.infrastructure.presentation.controller;

import com.arthurscarpin.acs.AccessControlSystemIntegrationTest;
import com.arthurscarpin.acs.core.owner.domain.DocumentType;
import com.arthurscarpin.acs.infrastructure.persistence.entity.table.OwnerEntity;
import com.arthurscarpin.acs.infrastructure.persistence.repository.table.OwnerRepository;
import com.arthurscarpin.acs.infrastructure.presentation.request.OwnerRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class OwnerControllerTest extends AccessControlSystemIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OwnerRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAllInBatch();

        OwnerEntity entity = OwnerEntity.builder()
                .name("Maria Oliveira")
                .document("111.444.777-35")
                .documentType(DocumentType.CPF)
                .email("contact1@example.com")
                .build();

        repository.save(entity);
    }

    @Test
    @DisplayName("Given valid owner request When saving Then returns 201 Created with owner data")
    @WithMockUser(authorities = {"SCOPE_admin:all", "SCOPE_owner:write"})
    void shouldCreateOwnerSuccessfully() throws Exception {
        OwnerRequest request = new OwnerRequest(
                "Paulo Silva",
                "222.454.857-40",
                DocumentType.CPF,
                "contact2@example.com"
        );

        mockMvc.perform(post("/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.email").value(request.email()))
                .andExpect(jsonPath("$.document").value(request.document()
                        .replace(".", "")
                        .replace("-", "")));
    }

    @Test
    @DisplayName("Given invalid owner request When saving Then returns 400 Bad Request")
    @WithMockUser(authorities = {"SCOPE_admin:all", "SCOPE_owner:write"})
    void shouldReturnBadRequestWhenInvalidData() throws Exception {
        OwnerRequest request = new OwnerRequest(
                "",
                "01234567891234567",
                null,
                "invalid-email"
        );

        mockMvc.perform(post("/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Given no authentication When saving owner Then returns 401 Unauthorized")
    void shouldReturnUnauthorizedWhenNoAuth() throws Exception {
        OwnerRequest request = new OwnerRequest(
                "Paulo Silva",
                "222.454.857-40",
                DocumentType.CPF,
                "contact@example.com"
        );

        mockMvc.perform(post("/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Given user without permission When saving owner Then returns 403 Forbidden")
    @WithMockUser(authorities = {"SCOPE_owner:read"})
    void shouldReturnForbiddenWhenNoPermission() throws Exception {
        OwnerRequest request = new OwnerRequest(
                "Paulo Silva",
                "222.454.857-40",
                DocumentType.CPF,
                "contact@example.com"
        );

        mockMvc.perform(post("/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Given authenticated user with permission When listing owners Then returns 200 OK with paginated data")
    @WithMockUser(authorities = {"SCOPE_admin:all", "SCOPE_owner:read"})
    void shouldReturnOwnersSuccessfully() throws Exception {

        mockMvc.perform(get("/owners")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").isNotEmpty())
                .andExpect(jsonPath("$.content[0].name").value("Maria Oliveira"))
                .andExpect(jsonPath("$.content[0].email").value("contact1@example.com"))
                .andExpect(jsonPath("$.content[0].document").value("111.444.777-35"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    @DisplayName("Given no authentication When listing owners Then returns 401 Unauthorized")
    void shouldReturnUnauthorizedWhenListingOwnersWithoutAuthentication() throws Exception {

        mockMvc.perform(get("/owners")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Given user without permission When listing owners Then returns 403 Forbidden")
    @WithMockUser(authorities = {"SCOPE_owner:write"})
    void shouldReturnForbiddenWhenListingOwnersWithoutPermission() throws Exception {

        mockMvc.perform(get("/owners")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Given no owners registered When listing owners Then returns empty page")
    @WithMockUser(authorities = {"SCOPE_admin:all", "SCOPE_owner:read"})
    void shouldReturnEmptyPageWhenNoOwnersExist() throws Exception {

        repository.deleteAllInBatch();

        mockMvc.perform(get("/owners")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));
    }
}