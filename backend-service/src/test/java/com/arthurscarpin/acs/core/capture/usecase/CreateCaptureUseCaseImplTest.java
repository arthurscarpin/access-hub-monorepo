package com.arthurscarpin.acs.core.capture.usecase;

import com.arthurscarpin.acs.core.capture.domain.*;
import com.arthurscarpin.acs.core.capture.gateway.CaptureGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCaptureUseCaseImplTest {

    @InjectMocks
    private CreateCaptureUseCaseImpl useCase;

    @Mock
    private CaptureGateway gateway;

    @Test
    @DisplayName("Given list of filenames, when executing use case, then should create capture and return id")
    void shouldCreateCaptureAndReturnId() {
        List<String> filenames = List.of("plate1.jpg", "plate2.jpg");
        String expectedId = UUID.randomUUID().toString();
        Capture savedCapture = createMockCapture(expectedId, filenames);

        when(gateway.saveAndPublish(any(Capture.class))).thenReturn(savedCapture);

        String result = useCase.execute(filenames);

        assertEquals(expectedId, result);
        verify(gateway, times(1)).saveAndPublish(any(Capture.class));
    }

    @Test
    @DisplayName("Given empty filename list, when executing use case, then should create capture with empty images")
    void shouldCreateCaptureWithEmptyImagesWhenNoFilenames() {
        List<String> filenames = List.of();
        String expectedId = UUID.randomUUID().toString();
        Capture savedCapture = createMockCapture(expectedId, filenames);

        when(gateway.saveAndPublish(any(Capture.class))).thenReturn(savedCapture);

        String result = useCase.execute(filenames);

        assertEquals(expectedId, result);
        verify(gateway, times(1)).saveAndPublish(any(Capture.class));
    }

    @Test
    @DisplayName("Given filenames, when executing use case, then should create capture images with RECEIVED status")
    void shouldCreateCaptureImagesWithReceivedStatus() {
        List<String> filenames = List.of("plate1.jpg");
        String expectedId = UUID.randomUUID().toString();
        Capture savedCapture = createMockCapture(expectedId, filenames);

        when(gateway.saveAndPublish(any(Capture.class))).thenReturn(savedCapture);

        useCase.execute(filenames);

        verify(gateway, times(1)).saveAndPublish(argThat(capture -> {
            assertNotNull(capture);
            assertEquals(CaptureStatus.RECEIVED, capture.status());
            assertNotNull(capture.images());
            assertEquals(1, capture.images().size());

            CaptureImage image = capture.images().get(0);
            assertEquals("plate1.jpg", image.filename());
            assertEquals(ImageStatus.RECEIVED, image.status());
            assertNull(image.ocrText());
            assertNull(image.confidence());
            assertNotNull(image.id());
            assertNull(image.timestamp());

            return true;
        }));
    }

    @Test
    @DisplayName("Given multiple filenames, when executing use case, then should create multiple capture images")
    void shouldCreateMultipleCaptureImages() {
        List<String> filenames = List.of("plate1.jpg", "plate2.jpg", "plate3.jpg");
        String expectedId = UUID.randomUUID().toString();
        Capture savedCapture = createMockCapture(expectedId, filenames);

        when(gateway.saveAndPublish(any(Capture.class))).thenReturn(savedCapture);

        useCase.execute(filenames);

        verify(gateway, times(1)).saveAndPublish(argThat(capture -> {
            assertNotNull(capture);
            assertEquals(3, capture.images().size());

            assertEquals("plate1.jpg", capture.images().get(0).filename());
            assertEquals("plate2.jpg", capture.images().get(1).filename());
            assertEquals("plate3.jpg", capture.images().get(2).filename());

            return true;
        }));
    }

    @Test
    @DisplayName("Given gateway failure, when executing use case, then should propagate exception")
    void shouldPropagateExceptionWhenGatewayFails() {
        List<String> filenames = List.of("plate1.jpg");

        when(gateway.saveAndPublish(any(Capture.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> useCase.execute(filenames));
        verify(gateway, times(1)).saveAndPublish(any(Capture.class));
    }

    @Test
    @DisplayName("Given valid execution, when executing use case, then should call gateway only once")
    void shouldCallGatewayOnlyOnce() {
        List<String> filenames = List.of("plate1.jpg");
        String expectedId = UUID.randomUUID().toString();
        Capture savedCapture = createMockCapture(expectedId, filenames);

        when(gateway.saveAndPublish(any(Capture.class))).thenReturn(savedCapture);

        useCase.execute(filenames);

        verify(gateway, times(1)).saveAndPublish(any(Capture.class));
        verifyNoMoreInteractions(gateway);
    }

    @Test
    @DisplayName("Given filenames, when executing use case, then should create capture with RECEIVED status and timestamps")
    void shouldCreateCaptureWithReceivedStatusAndTimestamps() {
        List<String> filenames = List.of("plate1.jpg");
        String expectedId = UUID.randomUUID().toString();
        Capture savedCapture = createMockCapture(expectedId, filenames);

        when(gateway.saveAndPublish(any(Capture.class))).thenReturn(savedCapture);

        useCase.execute(filenames);

        verify(gateway, times(1)).saveAndPublish(argThat(capture -> {
            assertNotNull(capture);
            assertEquals(CaptureStatus.RECEIVED, capture.status());
            assertNull(capture.finalPlate());
            assertNull(capture.finalConfidence());
            assertNotNull(capture.createdAt());
            assertNotNull(capture.updatedAt());
            assertNull(capture.processedAt());

            return true;
        }));
    }

    private Capture createMockCapture(String id, List<String> filenames) {
        List<CaptureImage> images = filenames.stream()
                .map(filename -> new CaptureImage(
                        UUID.randomUUID().toString(),
                        filename,
                        null,
                        null,
                        ImageStatus.RECEIVED,
                        null
                ))
                .toList();

        return new Capture(
                id,
                images,
                CaptureStatus.RECEIVED,
                null,
                null,
                Instant.now(),
                Instant.now(),
                null
        );
    }
}