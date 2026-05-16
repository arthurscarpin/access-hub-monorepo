package com.arthurscarpin.acs.infrastructure.presentation.consumer;

import com.arthurscarpin.acs.AccessControlSystemIntegrationTest;
import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import com.arthurscarpin.acs.core.capture.domain.*;
import com.arthurscarpin.acs.core.capture.usecase.ResultCaptureUseCase;
import com.arthurscarpin.acs.infrastructure.presentation.request.PlateRecognizedRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

class PlateRecognizedConsumerTest extends AccessControlSystemIntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @MockitoBean
    private ResultCaptureUseCase useCase;

    @Value("${spring.rabbitmq.ai-result-queue}")
    private String queueName;

    @Test
    @DisplayName("Given valid PlateRecognizedRequest when message is sent to queue then use case should be executed")
    void shouldConsumeMessageAndExecuteUseCase() {
        PlateRecognizedRequest request = createMockRequest();

        rabbitTemplate.convertAndSend(queueName, request);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
                verify(useCase).execute(any())
        );
    }

    @Test
    @DisplayName("Given valid payload when consuming then should map capture fields correctly to use case")
    void shouldMapCaptureFieldsCorrectlyWhenConsuming() {
        String captureId = UUID.randomUUID().toString();
        String imageId = UUID.randomUUID().toString();

        CaptureOCR ocr = new CaptureOCR("ABC1234", 0.98, List.of(List.of(10, 20, 30, 40)));
        CaptureImage image = new CaptureImage(imageId, "plate.jpg", ImageStatus.COMPLETED, List.of(ocr), Instant.now());
        Capture capture = new Capture(
                captureId,
                List.of(image),
                CaptureStatus.COMPLETED,
                Direction.IN,
                "ABC1234",
                0.98,
                "High confidence match",
                Instant.now(),
                Instant.now(),
                null,
                1,
                1L
        );

        PlateRecognizedRequest request = new PlateRecognizedRequest(capture, null);

        rabbitTemplate.convertAndSend(queueName, request);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
                verify(useCase).execute(argThat(c -> {
                    assertEquals(captureId, c.id());
                    assertEquals(CaptureStatus.COMPLETED, c.status());
                    assertEquals(Direction.IN, c.direction());
                    assertEquals("ABC1234", c.finalPlate());
                    assertEquals(0.98, c.finalConfidence());
                    assertEquals(1, c.processedImagesCount());
                    return true;
                }))
        );
    }

    @Test
    @DisplayName("Given capture with OUT direction when consuming then should map direction correctly")
    void shouldMapOutDirectionCorrectly() {
        PlateRecognizedRequest request = new PlateRecognizedRequest(buildCapture(Direction.OUT, "XYZ9876"), null);

        rabbitTemplate.convertAndSend(queueName, request);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
                verify(useCase).execute(argThat(c -> {
                    assertEquals(Direction.OUT, c.direction());
                    assertEquals("XYZ9876", c.finalPlate());
                    return true;
                }))
        );
    }

    private PlateRecognizedRequest createMockRequest() {
        return new PlateRecognizedRequest(buildCapture(Direction.IN, "ABC1234"), null);
    }

    private Capture buildCapture(Direction direction, String plate) {
        CaptureOCR ocr = new CaptureOCR(plate, 0.98, List.of(List.of(10, 20, 30, 40)));
        CaptureImage image = new CaptureImage(
                UUID.randomUUID().toString(),
                "plate.jpg",
                ImageStatus.COMPLETED,
                List.of(ocr),
                Instant.now()
        );
        return new Capture(
                UUID.randomUUID().toString(),
                List.of(image),
                CaptureStatus.COMPLETED,
                direction,
                plate,
                0.98,
                "High confidence match",
                Instant.now(),
                Instant.now(),
                null,
                1,
                1L
        );
    }
}