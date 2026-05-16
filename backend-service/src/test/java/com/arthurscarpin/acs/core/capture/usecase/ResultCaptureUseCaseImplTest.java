package com.arthurscarpin.acs.core.capture.usecase;

import com.arthurscarpin.acs.core.accessevent.domain.AccessEvent;
import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import com.arthurscarpin.acs.core.accessevent.usecase.ValidateAccessUseCase;
import com.arthurscarpin.acs.core.capture.domain.*;
import com.arthurscarpin.acs.core.capture.gateway.CaptureGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultCaptureUseCaseImplTest {

    @InjectMocks
    private ResultCaptureUseCaseImpl useCase;

    @Mock
    private CaptureGateway gateway;

    @Mock
    private ValidateAccessUseCase validateAccessUseCase;

    private Capture buildCapture() {
        CaptureOCR ocr = new CaptureOCR(
                "ABC1234",
                0.98,
                List.of(List.of(10, 20, 30, 40))
        );

        CaptureImage image = new CaptureImage(
                "image-id-1",
                "image1.jpg",
                ImageStatus.COMPLETED,
                List.of(ocr),
                Instant.parse("2024-01-01T10:00:00Z")
        );

        return new Capture(
                "capture-id-123",
                List.of(image),
                CaptureStatus.COMPLETED,
                Direction.IN,
                "ABC1234",
                0.98,
                "High confidence match",
                Instant.parse("2024-01-01T10:00:00Z"),
                Instant.parse("2024-01-01T10:01:00Z"),
                null,
                1,
                1L
        );
    }

    @Test
    @DisplayName("Given valid capture, when executing use case, then should update capture with processedAt timestamp")
    void shouldUpdateCaptureWithProcessedAt() {
        Capture capture = buildCapture();

        Instant before = Instant.now();
        useCase.execute(capture);
        Instant after = Instant.now();

        ArgumentCaptor<Capture> captor = ArgumentCaptor.forClass(Capture.class);
        verify(gateway).update(captor.capture());

        Capture updated = captor.getValue();
        assertNotNull(updated.processedAt());
        assertFalse(updated.processedAt().isBefore(before));
        assertFalse(updated.processedAt().isAfter(after));
    }

    @Test
    @DisplayName("Given valid capture, when executing use case, then should preserve all original fields on update")
    void shouldPreserveOriginalFieldsOnUpdate() {
        Capture capture = buildCapture();

        useCase.execute(capture);

        ArgumentCaptor<Capture> captor = ArgumentCaptor.forClass(Capture.class);
        verify(gateway).update(captor.capture());

        Capture updated = captor.getValue();
        assertEquals(capture.id(), updated.id());
        assertEquals(capture.images(), updated.images());
        assertEquals(capture.status(), updated.status());
        assertEquals(capture.direction(), updated.direction());
        assertEquals(capture.finalPlate(), updated.finalPlate());
        assertEquals(capture.finalConfidence(), updated.finalConfidence());
        assertEquals(capture.reasoning(), updated.reasoning());
        assertEquals(capture.createdAt(), updated.createdAt());
        assertEquals(capture.updatedAt(), updated.updatedAt());
        assertEquals(capture.processedImagesCount(), updated.processedImagesCount());
        assertEquals(capture.version(), updated.version());
    }

    @Test
    @DisplayName("Given valid capture, when executing use case, then should call gateway update exactly once")
    void shouldCallGatewayUpdateOnce() {
        Capture capture = buildCapture();

        useCase.execute(capture);

        verify(gateway, times(1)).update(any(Capture.class));
        verifyNoMoreInteractions(gateway);
    }

    @Test
    @DisplayName("Given valid capture, when executing use case, then should call validateAccessUseCase exactly once")
    void shouldCallValidateAccessUseCaseOnce() {
        Capture capture = buildCapture();

        useCase.execute(capture);

        verify(validateAccessUseCase, times(1)).execute(any(AccessEvent.class));
        verifyNoMoreInteractions(validateAccessUseCase);
    }

    @Test
    @DisplayName("Given valid capture, when executing use case, then should create AccessEvent with correct plate and direction")
    void shouldCreateAccessEventWithCorrectPlateAndDirection() {
        Capture capture = buildCapture();

        useCase.execute(capture);

        ArgumentCaptor<AccessEvent> captor = ArgumentCaptor.forClass(AccessEvent.class);
        verify(validateAccessUseCase).execute(captor.capture());

        AccessEvent event = captor.getValue();
        assertEquals("ABC1234", event.plate());
        assertEquals(Direction.IN, event.direction());
    }

    @Test
    @DisplayName("Given valid capture, when executing use case, then AccessEvent id should be null")
    void shouldCreateAccessEventWithNullId() {
        Capture capture = buildCapture();

        useCase.execute(capture);

        ArgumentCaptor<AccessEvent> captor = ArgumentCaptor.forClass(AccessEvent.class);
        verify(validateAccessUseCase).execute(captor.capture());

        assertNull(captor.getValue().id());
    }

    @Test
    @DisplayName("Given valid capture, when executing use case, then AccessEvent result should be null")
    void shouldCreateAccessEventWithNullResult() {
        Capture capture = buildCapture();

        useCase.execute(capture);

        ArgumentCaptor<AccessEvent> captor = ArgumentCaptor.forClass(AccessEvent.class);
        verify(validateAccessUseCase).execute(captor.capture());

        assertNull(captor.getValue().result());
    }

    @Test
    @DisplayName("Given valid capture, when executing use case, then AccessEvent timestamp should be close to now")
    void shouldCreateAccessEventWithTimestampCloseToNow() {
        Capture capture = buildCapture();

        OffsetDateTime before = OffsetDateTime.now();
        useCase.execute(capture);
        OffsetDateTime after = OffsetDateTime.now();

        ArgumentCaptor<AccessEvent> captor = ArgumentCaptor.forClass(AccessEvent.class);
        verify(validateAccessUseCase).execute(captor.capture());

        AccessEvent event = captor.getValue();
        assertNotNull(event.timestamp());
        assertFalse(event.timestamp().isBefore(before));
        assertFalse(event.timestamp().isAfter(after));
    }

    @Test
    @DisplayName("Given valid capture, when executing use case, then should call gateway before validateAccessUseCase")
    void shouldCallGatewayBeforeValidateAccessUseCase() {
        Capture capture = buildCapture();

        var order = inOrder(gateway, validateAccessUseCase);
        useCase.execute(capture);

        order.verify(gateway).update(any(Capture.class));
        order.verify(validateAccessUseCase).execute(any(AccessEvent.class));
    }

    @Test
    @DisplayName("Given gateway throws exception, when executing use case, then should propagate exception and not call validateAccessUseCase")
    void shouldPropagateGatewayExceptionAndSkipValidation() {
        Capture capture = buildCapture();
        doThrow(new RuntimeException("Database error")).when(gateway).update(any(Capture.class));

        assertThrows(RuntimeException.class, () -> useCase.execute(capture));

        verify(gateway, times(1)).update(any(Capture.class));
        verifyNoInteractions(validateAccessUseCase);
    }

    @Test
    @DisplayName("Given validateAccessUseCase throws exception, when executing use case, then should propagate exception")
    void shouldPropagateValidateAccessUseCaseException() {
        Capture capture = buildCapture();
        doThrow(new RuntimeException("Validation error")).when(validateAccessUseCase).execute(any(AccessEvent.class));

        assertThrows(RuntimeException.class, () -> useCase.execute(capture));

        verify(gateway, times(1)).update(any(Capture.class));
        verify(validateAccessUseCase, times(1)).execute(any(AccessEvent.class));
    }
}