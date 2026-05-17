package com.arthurscarpin.acs.core.capture.usecase;

import com.arthurscarpin.acs.core.accessevent.domain.Direction;
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
class StatusCaptureUseCaseImplTest {

    @InjectMocks
    private StatusCaptureUseCaseImpl useCase;

    @Mock
    private CaptureGateway gateway;

//    @Test
//    @DisplayName("Given valid status request, when executing use case, then should update specific image and publish capture")
//    void shouldUpdateSpecificImageAndPublish() {
//        String captureId = UUID.randomUUID().toString();
//        String targetImageId = UUID.randomUUID().toString();
//        String otherImageId = UUID.randomUUID().toString();
//
//        CaptureOCRStatus request = createMockRequest(captureId, targetImageId);
//        Capture existingCapture = createMockCapture(captureId, List.of(targetImageId, otherImageId));
//
//        when(gateway.findByCaptureIdAndImageId(captureId, targetImageId)).thenReturn(existingCapture);
//
//        useCase.execute(request);
//
//        verify(gateway, times(1)).updateAndPublish(argThat(capture -> {
//            CaptureImage updatedImg = capture.images().stream()
//                    .filter(img -> img.id().equals(targetImageId))
//                    .findFirst().orElseThrow();
//
//            CaptureImage unchangedImg = capture.images().stream()
//                    .filter(img -> img.id().equals(otherImageId))
//                    .findFirst().orElseThrow();
//
//            assertEquals(request.imageStatus(), updatedImg.status());
//            assertEquals(request.ocr().size(), updatedImg.ocr().size());
//            assertEquals(request.captureStatus(), capture.status());
//            assertNotNull(updatedImg.timestamp());
//            assertEquals(ImageStatus.RECEIVED, unchangedImg.status());
//
//            return true;
//        }));
//    }
//
//    @Test
//    @DisplayName("Given null OCR in request, when executing use case, then should update image with empty OCR list")
//    void shouldUpdateWithEmptyOcrWhenRequestOcrIsNull() {
//        String captureId = UUID.randomUUID().toString();
//        String imageId = UUID.randomUUID().toString();
//
//        CaptureOCRStatus request = new CaptureOCRStatus(
//                captureId,
//                imageId,
//                "image.jpg",
//                Instant.now(),
//                ImageStatus.COMPLETED,
//                CaptureStatus.COMPLETED,
//                "Success",
//                null
//        );
//
//        Capture existingCapture = createMockCapture(captureId, List.of(imageId));
//        when(gateway.findByCaptureIdAndImageId(captureId, imageId)).thenReturn(existingCapture);
//
//        useCase.execute(request);
//
//        verify(gateway).updateAndPublish(argThat(capture -> {
//            CaptureImage img = capture.images().get(0);
//            assertNotNull(img.ocr());
//            assertTrue(img.ocr().isEmpty());
//            return true;
//        }));
//    }
//
//    @Test
//    @DisplayName("Given gateway error on find, when executing use case, then should propagate exception")
//    void shouldPropagateExceptionWhenGatewayFails() {
//        CaptureOCRStatus request = createMockRequest(UUID.randomUUID().toString(), UUID.randomUUID().toString());
//
//        when(gateway.findByCaptureIdAndImageId(any(), any()))
//                .thenThrow(new RuntimeException("Capture not found"));
//
//        assertThrows(RuntimeException.class, () -> useCase.execute(request));
//        verify(gateway, never()).updateAndPublish(any());
//    }
//
//    private CaptureOCRStatus createMockRequest(String captureId, String imageId) {
//        List<List<Integer>> bbox = List.of(
//                List.of(10, 10),
//                List.of(50, 10),
//                List.of(50, 30),
//                List.of(10, 30)
//        );
//
//        return new CaptureOCRStatus(
//                captureId,
//                imageId,
//                "plate.jpg",
//                Instant.now(),
//                ImageStatus.COMPLETED,
//                CaptureStatus.COMPLETED,
//                "Processed successfully",
//                List.of(new CaptureOCR("ABC1234", 0.99, bbox))
//        );
//    }
//
//    private Capture createMockCapture(String id, List<String> imageIds) {
//        List<CaptureImage> images = imageIds.stream()
//                .map(imageId -> new CaptureImage(
//                        imageId,
//                        "image.jpg",
//                        ImageStatus.RECEIVED,
//                        List.of(),
//                        null
//                ))
//                .toList();
//
//        return new Capture(
//                id,
//                images,
//                CaptureStatus.RECEIVED,
//                Direction.IN,
//                null,
//                null,
//                null,
//                Instant.now(),
//                null,
//                null,
//                0,
//                1L
//        );
//    }
}