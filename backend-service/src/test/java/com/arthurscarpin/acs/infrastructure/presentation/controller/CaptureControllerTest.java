package com.arthurscarpin.acs.infrastructure.presentation.controller;

import com.arthurscarpin.acs.AccessControlSystemIntegrationTest;
import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import com.arthurscarpin.acs.core.capture.domain.Capture;
import com.arthurscarpin.acs.core.capture.domain.CaptureImage;
import com.arthurscarpin.acs.core.capture.domain.CaptureStatus;
import com.arthurscarpin.acs.core.capture.domain.ImageStatus;
import com.arthurscarpin.acs.core.capture.usecase.CreateCaptureUseCase;
import com.arthurscarpin.acs.infrastructure.presentation.request.CaptureRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CaptureControllerTest extends AccessControlSystemIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateCaptureUseCase createCaptureUseCase;

    @Test
    @DisplayName("Given valid capture request When saving Then returns 201 Created with capture data")
    @WithMockUser(authorities = {"SCOPE_admin:all", "SCOPE_capture:write"})
    void shouldCreateCaptureSuccessfully() throws Exception {
        String captureId = UUID.randomUUID().toString();
        String filename = captureId + ".zip";
        CaptureRequest request = new CaptureRequest(filename, Direction.IN);

        when(createCaptureUseCase.execute(filename, Direction.IN))
                .thenReturn(createMockCapture(captureId, List.of("plate1.jpg", "plate2.jpg")));

        mockMvc.perform(post("/captures/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(captureId))
                .andExpect(jsonPath("$.message")
                        .value("Capture created with success"));
    }

    @Test
    @DisplayName("Given capture request with zip filename When saving Then returns 201 Created")
    @WithMockUser(authorities = {"SCOPE_capture:write"})
    void shouldCreateCaptureWithZipFilename() throws Exception {
        String captureId = UUID.randomUUID().toString();
        String filename = captureId + ".zip";
        CaptureRequest request = new CaptureRequest(filename, Direction.IN);

        when(createCaptureUseCase.execute(filename, Direction.IN))
                .thenReturn(createMockCapture(captureId, List.of("license_plate.jpg")));

        mockMvc.perform(post("/captures/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(captureId))
                .andExpect(jsonPath("$.message")
                        .value("Capture created with success"));
    }

    @Test
    @DisplayName("Given capture request with extracted files When saving Then returns 201 Created")
    @WithMockUser(authorities = {"SCOPE_admin:all"})
    void shouldCreateCaptureWithExtractedFiles() throws Exception {
        String captureId = UUID.randomUUID().toString();
        String filename = captureId + ".zip";
        CaptureRequest request = new CaptureRequest(filename, Direction.IN);

        when(createCaptureUseCase.execute(filename, Direction.IN))
                .thenReturn(createMockCapture(captureId, List.of("plate1.jpg", "plate2.jpg", "plate3.jpg")));

        mockMvc.perform(post("/captures/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(captureId))
                .andExpect(jsonPath("$.message")
                        .value("Capture created with success"))
                .andExpect(jsonPath("$.extractedFiles.length()").value(3));
    }

    @Test
    @DisplayName("Given invalid capture request with empty filename When saving Then returns 400 Bad Request")
    @WithMockUser(authorities = {"SCOPE_capture:write"})
    void shouldReturnBadRequestWhenEmptyFilename() throws Exception {
        CaptureRequest request =
                new CaptureRequest("", null);

        mockMvc.perform(post("/captures/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Given invalid capture request with null filename When saving Then returns 400 Bad Request")
    @WithMockUser(authorities = {"SCOPE_capture:write"})
    void shouldReturnBadRequestWhenNullFilename() throws Exception {
        String invalidRequest = """
                {
                  "filename": null
                }
                """;

        mockMvc.perform(post("/captures/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Given no authentication When saving capture Then returns 401 Unauthorized")
    void shouldReturnUnauthorizedWhenNoAuth() throws Exception {
        String captureId = UUID.randomUUID().toString();
        CaptureRequest request =
                new CaptureRequest(captureId + ".zip", Direction.IN);

        mockMvc.perform(post("/captures/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Given user without permission When saving capture Then returns 403 Forbidden")
    @WithMockUser(authorities = {"SCOPE_capture:read"})
    void shouldReturnForbiddenWhenNoPermission() throws Exception {
        String captureId = UUID.randomUUID().toString();
        CaptureRequest request =
                new CaptureRequest(captureId + ".zip", Direction.IN);

        mockMvc.perform(post("/captures/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Given user with only capture:read scope When saving capture Then returns 403 Forbidden")
    @WithMockUser(authorities = {"SCOPE_capture:read", "SCOPE_admin:read"})
    void shouldReturnForbiddenWhenOnlyReadPermission() throws Exception {
        String captureId = UUID.randomUUID().toString();
        CaptureRequest request =
                new CaptureRequest(captureId + ".zip", Direction.IN);

        mockMvc.perform(post("/captures/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    private Capture createMockCapture(String id, List<String> filenames) {
        List<CaptureImage> images = filenames.stream()
                .map(filename -> new CaptureImage(
                        UUID.randomUUID().toString(),
                        filename,
                        ImageStatus.RECEIVED,
                        List.of(),
                        Instant.now()
                ))
                .toList();

        return new Capture(
                id,
                images,
                CaptureStatus.RECEIVED,
                Direction.IN,
                null,
                null,
                null,
                Instant.now(),
                Instant.now(),
                null,
                0,
                1L
        );
    }
}
