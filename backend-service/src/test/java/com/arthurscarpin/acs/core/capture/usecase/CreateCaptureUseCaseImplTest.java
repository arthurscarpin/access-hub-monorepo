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
    @DisplayName("Given filenames, when executing use case, then should create capture images with RECEIVED status")
    void shouldCreateCaptureImagesWithReceivedStatus() {
        List<String> filenames = List.of("plate1.jpg");
        String expectedId = UUID.randomUUID().toString();
        Capture savedCapture = createMockCapture(expectedId, filenames);

        when(gateway.saveAndPublish(any(Capture.class))).thenReturn(savedCapture);

        useCase.execute(filenames);

        verify(gateway, times(1)).saveAndPublish(argThat(capture -> {
            assertNotNull(capture);
            CaptureImage image = capture.images().get(0);

            assertEquals("plate1.jpg", image.filename());
            assertEquals(ImageStatus.RECEIVED, image.status());
            assertNotNull(image.ocr());
            assertTrue(image.ocr().isEmpty());

            return true;
        }));
    }

    @Test
    @DisplayName("Given multiple filenames, when executing use case, then should create capture with correct counts")
    void shouldCreateCaptureWithCorrectCounts() {
        List<String> filenames = List.of("img1.jpg", "img2.jpg");
        String expectedId = UUID.randomUUID().toString();
        Capture savedCapture = createMockCapture(expectedId, filenames);

        when(gateway.saveAndPublish(any(Capture.class))).thenReturn(savedCapture);

        useCase.execute(filenames);

        verify(gateway, times(1)).saveAndPublish(argThat(capture -> {
            assertEquals(2, capture.images().size());
            assertEquals(0, capture.processedImagesCount());
            return true;
        }));
    }

    @Test
    @DisplayName("Given gateway failure, when executing use case, then should propagate exception")
    void shouldPropagateExceptionWhenGatewayFails() {
        List<String> filenames = List.of("plate1.jpg");
        when(gateway.saveAndPublish(any(Capture.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> useCase.execute(filenames));
    }

    private Capture createMockCapture(String id, List<String> filenames) {
        List<CaptureImage> images = filenames.stream()
                .map(filename -> new CaptureImage(
                        UUID.randomUUID().toString(),
                        filename,
                        ImageStatus.RECEIVED,
                        List.of(),
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
                null,
                0,
                1L
        );
    }
}