package com.arthurscarpin.acs.core.capture.usecase;

import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import com.arthurscarpin.acs.core.capture.domain.*;
import com.arthurscarpin.acs.core.capture.exception.CaptureZipException;
import com.arthurscarpin.acs.core.capture.gateway.CaptureGateway;
import com.arthurscarpin.acs.core.capture.gateway.CaptureZipGateway;
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

    @Mock
    private CaptureZipGateway zipGateway;

    @Test
    @DisplayName("Given zip filename, when executing use case, then should create capture and return id")
    void shouldCreateCaptureAndReturnId() {
        String captureId = UUID.randomUUID().toString();
        String filename = captureId + ".zip";
        List<String> filenames = List.of("plate1.jpg", "plate2.jpg");
        Direction direction = Direction.IN;
        Capture savedCapture = createMockCapture(captureId, filenames);

        when(zipGateway.zipProcessCapture(captureId)).thenReturn(filenames);
        when(gateway.saveAndPublish(any(Capture.class))).thenReturn(savedCapture);

        Capture result = useCase.execute(filename, direction);

        assertEquals(captureId, result.id());
        verify(gateway, times(1)).saveAndPublish(any(Capture.class));
    }

    @Test
    @DisplayName("Given filename, when executing use case, then should create capture images with RECEIVED status")
    void shouldCreateCaptureImagesWithReceivedStatus() {
        String captureId = UUID.randomUUID().toString();
        String filename = captureId + ".zip";
        List<String> filenames = List.of("plate1.jpg");
        Direction direction = Direction.IN;
        Capture savedCapture = createMockCapture(captureId, filenames);

        when(zipGateway.zipProcessCapture(captureId)).thenReturn(filenames);
        when(gateway.saveAndPublish(any(Capture.class))).thenReturn(savedCapture);

        useCase.execute(filename, direction);

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
    @DisplayName("Given multiple extracted files, when executing use case, then should create capture with correct counts")
    void shouldCreateCaptureWithCorrectCounts() {
        String captureId = UUID.randomUUID().toString();
        String filename = captureId + ".zip";
        List<String> filenames = List.of("img1.jpg", "img2.jpg");
        Direction direction = Direction.IN;
        Capture savedCapture = createMockCapture(captureId, filenames);

        when(zipGateway.zipProcessCapture(captureId)).thenReturn(filenames);
        when(gateway.saveAndPublish(any(Capture.class))).thenReturn(savedCapture);

        useCase.execute(filename, direction);

        verify(gateway, times(1)).saveAndPublish(argThat(capture -> {
            assertEquals(2, capture.images().size());
            assertEquals(0, capture.processedImagesCount());
            return true;
        }));
    }

    @Test
    @DisplayName("Given gateway failure, when executing use case, then should propagate exception")
    void shouldPropagateExceptionWhenGatewayFails() {
        String captureId = UUID.randomUUID().toString();
        String filename = captureId + ".zip";
        List<String> filenames = List.of("plate1.jpg");
        Direction direction = Direction.IN;

        when(zipGateway.zipProcessCapture(captureId)).thenReturn(filenames);
        when(gateway.saveAndPublish(any(Capture.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> useCase.execute(filename, direction));
    }

    @Test
    @DisplayName("Given zip processing failure, when executing use case, then should return failed capture")
    void shouldReturnFailedCaptureWhenZipProcessingFails() {
        String captureId = UUID.randomUUID().toString();
        String filename = captureId + ".zip";
        Direction direction = Direction.IN;

        when(zipGateway.zipProcessCapture(captureId)).thenThrow(new CaptureZipException("ZIP Error"));

        Capture result = useCase.execute(filename, direction);

        assertEquals(captureId, result.id());
        assertEquals(CaptureStatus.FAILED, result.status());
        assertTrue(result.images().isEmpty());
        verify(gateway, never()).saveAndPublish(any(Capture.class));
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
