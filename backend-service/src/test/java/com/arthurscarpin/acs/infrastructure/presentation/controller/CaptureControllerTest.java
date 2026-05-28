package com.arthurscarpin.acs.infrastructure.presentation.controller;

import com.arthurscarpin.acs.AccessControlSystemIntegrationTest;
import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import com.arthurscarpin.acs.core.capture.domain.Capture;
import com.arthurscarpin.acs.core.capture.domain.CaptureImage;
import com.arthurscarpin.acs.core.capture.domain.CaptureStatus;
import com.arthurscarpin.acs.core.capture.domain.ImageStatus;
import com.arthurscarpin.acs.core.capture.usecase.CreateCaptureUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "storage.root=/tmp/acs-capture-controller-test")
class CaptureControllerTest extends AccessControlSystemIntegrationTest {

    @MockitoBean
    private CreateCaptureUseCase createCaptureUseCase;

    @Test
    @DisplayName("Given valid capture request When saving Then returns 201 Created with capture data")
    @WithMockUser(authorities = {"SCOPE_admin:all", "SCOPE_capture:write"})
    void shouldCreateCaptureSuccessfully() throws Exception {
        String captureId = UUID.randomUUID().toString();
        String filename = captureId + ".zip";
        MockMultipartFile file = zipFile(filename);

        when(createCaptureUseCase.execute(filename, Direction.IN))
                .thenReturn(createMockCapture(captureId, List.of("plate1.jpg", "plate2.jpg")));

        mockMvc.perform(multipart("/captures/upload")
                        .file(file)
                        .param("direction", Direction.IN.name())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(captureId))
                .andExpect(jsonPath("$.message")
                        .value("Capture created with success"));

        verify(createCaptureUseCase).execute(filename, Direction.IN);
    }

    @Test
    @DisplayName("Given capture request with zip filename When saving Then returns 201 Created")
    @WithMockUser(authorities = {"SCOPE_capture:write"})
    void shouldCreateCaptureWithZipFilename() throws Exception {
        String captureId = UUID.randomUUID().toString();
        String filename = captureId + ".zip";
        MockMultipartFile file = zipFile(filename);

        when(createCaptureUseCase.execute(filename, Direction.IN))
                .thenReturn(createMockCapture(captureId, List.of("license_plate.jpg")));

        mockMvc.perform(multipart("/captures/upload")
                        .file(file)
                        .param("direction", Direction.IN.name())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
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
        MockMultipartFile file = zipFile(filename);

        when(createCaptureUseCase.execute(filename, Direction.IN))
                .thenReturn(createMockCapture(captureId, List.of("plate1.jpg", "plate2.jpg", "plate3.jpg")));

        mockMvc.perform(multipart("/captures/upload")
                        .file(file)
                        .param("direction", Direction.IN.name())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
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
        MockMultipartFile file = zipFile("");

        mockMvc.perform(multipart("/captures/upload")
                        .file(file)
                        .param("direction", Direction.IN.name())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Given invalid capture request with null filename When saving Then returns 400 Bad Request")
    @WithMockUser(authorities = {"SCOPE_capture:write"})
    void shouldReturnBadRequestWhenNullFilename() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                null,
                "application/zip",
                "zip-content".getBytes()
        );

        mockMvc.perform(multipart("/captures/upload")
                        .file(file)
                        .param("direction", Direction.IN.name())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Given no authentication When saving capture Then returns 401 Unauthorized")
    void shouldReturnUnauthorizedWhenNoAuth() throws Exception {
        String captureId = UUID.randomUUID().toString();
        MockMultipartFile file = zipFile(captureId + ".zip");

        mockMvc.perform(multipart("/captures/upload")
                        .file(file)
                        .param("direction", Direction.IN.name())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Given user without permission When saving capture Then returns 403 Forbidden")
    @WithMockUser(authorities = {"SCOPE_capture:read"})
    void shouldReturnForbiddenWhenNoPermission() throws Exception {
        String captureId = UUID.randomUUID().toString();
        MockMultipartFile file = zipFile(captureId + ".zip");

        mockMvc.perform(multipart("/captures/upload")
                        .file(file)
                        .param("direction", Direction.IN.name())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Given user with only capture:read scope When saving capture Then returns 403 Forbidden")
    @WithMockUser(authorities = {"SCOPE_capture:read", "SCOPE_admin:read"})
    void shouldReturnForbiddenWhenOnlyReadPermission() throws Exception {
        String captureId = UUID.randomUUID().toString();
        MockMultipartFile file = zipFile(captureId + ".zip");

        mockMvc.perform(multipart("/captures/upload")
                        .file(file)
                        .param("direction", Direction.IN.name())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isForbidden());
    }

    private MockMultipartFile zipFile(String filename) {
        return new MockMultipartFile(
                "file",
                filename,
                "application/zip",
                "zip-content".getBytes()
        );
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
