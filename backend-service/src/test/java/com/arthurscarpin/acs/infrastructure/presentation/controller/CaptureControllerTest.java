package com.arthurscarpin.acs.infrastructure.presentation.controller;

import com.arthurscarpin.acs.AccessControlSystemIntegrationTest;
import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CaptureControllerTest extends AccessControlSystemIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Given valid capture request When saving Then returns 201 Created with capture data")
    @WithMockUser(authorities = {"SCOPE_admin:all", "SCOPE_capture:write"})
    void shouldCreateCaptureSuccessfully() throws Exception {
        CaptureRequest request = new CaptureRequest(List.of("plate1.jpg", "plate2.jpg"), Direction.IN);

        mockMvc.perform(post("/captures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.captureId").isNotEmpty())
                .andExpect(jsonPath("$.message")
                        .value("Capture registered successfully"));
    }

    @Test
    @DisplayName("Given capture request with single filename When saving Then returns 201 Created")
    @WithMockUser(authorities = {"SCOPE_capture:write"})
    void shouldCreateCaptureWithSingleFilename() throws Exception {
        CaptureRequest request = new CaptureRequest(List.of("license_plate.jpg"), Direction.IN);

        mockMvc.perform(post("/captures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.captureId").isNotEmpty())
                .andExpect(jsonPath("$.message")
                        .value("Capture registered successfully"));
    }

    @Test
    @DisplayName("Given capture request with multiple filenames When saving Then returns 201 Created")
    @WithMockUser(authorities = {"SCOPE_admin:all"})
    void shouldCreateCaptureWithMultipleFilenames() throws Exception {
        CaptureRequest request =
                new CaptureRequest(List.of(
                        "plate1.jpg",
                        "plate2.jpg",
                        "plate3.jpg"
                ), Direction.IN);

        mockMvc.perform(post("/captures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.captureId").isNotEmpty())
                .andExpect(jsonPath("$.message")
                        .value("Capture registered successfully"));
    }

    @Test
    @DisplayName("Given invalid capture request with empty filenames When saving Then returns 400 Bad Request")
    @WithMockUser(authorities = {"SCOPE_capture:write"})
    void shouldReturnBadRequestWhenEmptyFilenames() throws Exception {
        CaptureRequest request =
                new CaptureRequest(List.of(), null);

        mockMvc.perform(post("/captures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Given invalid capture request with null filenames When saving Then returns 400 Bad Request")
    @WithMockUser(authorities = {"SCOPE_capture:write"})
    void shouldReturnBadRequestWhenNullFilenames() throws Exception {
        String invalidRequest = """
                {
                  "filenames": null
                }
                """;

        mockMvc.perform(post("/captures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Given no authentication When saving capture Then returns 401 Unauthorized")
    void shouldReturnUnauthorizedWhenNoAuth() throws Exception {
        CaptureRequest request =
                new CaptureRequest(List.of("plate1.jpg"), Direction.IN);

        mockMvc.perform(post("/captures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Given user without permission When saving capture Then returns 403 Forbidden")
    @WithMockUser(authorities = {"SCOPE_capture:read"})
    void shouldReturnForbiddenWhenNoPermission() throws Exception {
        CaptureRequest request =
                new CaptureRequest(List.of("plate1.jpg"), Direction.IN);

        mockMvc.perform(post("/captures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Given user with only capture:read scope When saving capture Then returns 403 Forbidden")
    @WithMockUser(authorities = {"SCOPE_capture:read", "SCOPE_admin:read"})
    void shouldReturnForbiddenWhenOnlyReadPermission() throws Exception {
        CaptureRequest request =
                new CaptureRequest(List.of("plate1.jpg"), Direction.IN);

        mockMvc.perform(post("/captures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}