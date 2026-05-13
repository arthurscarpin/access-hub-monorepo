package com.arthurscarpin.acs.infrastructure.presentation.consumer;

import com.arthurscarpin.acs.AccessControlSystemIntegrationTest;
import com.arthurscarpin.acs.core.capture.domain.CaptureStatus;
import com.arthurscarpin.acs.core.capture.domain.ImageStatus;
import com.arthurscarpin.acs.core.capture.usecase.StatusCaptureUseCase;
import com.arthurscarpin.acs.infrastructure.presentation.request.CaptureOCRStatusRequest;
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

class CaptureStatusConsumerTest extends AccessControlSystemIntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @MockitoBean
    private StatusCaptureUseCase useCase;

    @Value("${spring.rabbitmq.ocr-status-queue}")
    private String queueName;

    @Test
    @DisplayName("Given valid CaptureOCRStatusRequest When message is sent to queue Then use case should be executed")
    void shouldConsumeMessageAndExecuteUseCase() {
        CaptureOCRStatusRequest request = createMockRequest();

        rabbitTemplate.convertAndSend(queueName, request);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
                verify(useCase).execute(any())
        );
    }

    @Test
    @DisplayName("Given valid payload When consuming Then should map fields correctly to use case")
    void shouldMapFieldsCorrectlyWhenConsuming() {
        String captureId = UUID.randomUUID().toString();
        String imageId = UUID.randomUUID().toString();
        CaptureOCRStatusRequest request = new CaptureOCRStatusRequest(
                captureId,
                imageId,
                "plate.jpg",
                Instant.now(),
                ImageStatus.COMPLETED,
                CaptureStatus.COMPLETED,
                "Processed",
                List.of()
        );

        rabbitTemplate.convertAndSend(queueName, request);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
                verify(useCase).execute(argThat(status -> {
                    assertEquals(captureId, status.captureId());
                    assertEquals(imageId, status.imageId());
                    assertEquals(CaptureStatus.COMPLETED, status.captureStatus());
                    assertEquals(ImageStatus.COMPLETED, status.imageStatus());
                    return true;
                }))
        );
    }

    private CaptureOCRStatusRequest createMockRequest() {
        return new CaptureOCRStatusRequest(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "test.jpg",
                Instant.now(),
                ImageStatus.PROCESSING,
                CaptureStatus.PROCESSING,
                "Normal flow",
                List.of()
        );
    }
}