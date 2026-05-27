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

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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

    @Test
    @DisplayName("Given existing users When listing users Then returns 200 OK with paginated users")
    @WithMockUser(authorities = {"SCOPE_admin:all"})
    void shouldReturnPaginatedUsersSuccessfully() throws Exception {

        UserEntity userOne = new UserEntity(
                null,
                "Ana Santos",
                "ana@email.com",
                "Password@123",
                true,
                List.of(scopeRepository.findById(scopeId).orElseThrow())
        );

        UserEntity userTwo = new UserEntity(
                null,
                "Carlos Silva",
                "carlos@email.com",
                "Password@123",
                true,
                List.of(scopeRepository.findById(scopeId).orElseThrow())
        );

        userRepository.saveAll(List.of(userOne, userTwo));

        mockMvc.perform(get("/users")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    @DisplayName("Given no users When listing users Then returns empty page")
    @WithMockUser(authorities = {"SCOPE_admin:all"})
    void shouldReturnEmptyPageWhenNoUsersExist() throws Exception {

        userRepository.deleteAllInBatch();

        mockMvc.perform(get("/users")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    @DisplayName("Given pagination params When listing users Then should paginate correctly")
    @WithMockUser(authorities = {"SCOPE_admin:all"})
    void shouldPaginateUsersCorrectly() throws Exception {

        for (int i = 0; i < 15; i++) {
            UserEntity user = new UserEntity(
                    null,
                    "User " + i,
                    "user" + i + "@email.com",
                    "Password@123",
                    true,
                    List.of(scopeRepository.findById(scopeId).orElseThrow())
            );

            userRepository.save(user);
        }

        mockMvc.perform(get("/users")
                        .param("page", "0")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(5))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.totalElements").value(16))
                .andExpect(jsonPath("$.totalPages").value(4));
    }

    @Test
    @DisplayName("Given page out of bounds When listing users Then returns empty content")
    @WithMockUser(authorities = {"SCOPE_admin:all"})
    void shouldReturnEmptyContentWhenPageOutOfBounds() throws Exception {

        mockMvc.perform(get("/users")
                        .param("page", "999")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    @DisplayName("Given request without authentication When listing users Then returns 401 Unauthorized")
    void shouldReturnUnauthorizedWhenUserIsNotAuthenticated() throws Exception {

        mockMvc.perform(get("/users")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Given user without permission When listing users Then returns 403 Forbidden")
    @WithMockUser(authorities = {"SCOPE_user:read"})
    void shouldReturnForbiddenWhenUserDoesNotHavePermission() throws Exception {

        mockMvc.perform(get("/users")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}