package com.arthurscarpin.acs.infrastructure.presentation.consumer;

import com.arthurscarpin.acs.core.capture.usecase.StatusCaptureUseCase;
import com.arthurscarpin.acs.infrastructure.mapper.CaptureMapper;
import com.arthurscarpin.acs.infrastructure.presentation.request.CaptureOCRStatusRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CaptureStatusConsumer {

    private final StatusCaptureUseCase useCase;

    private final CaptureMapper mapper;

    @RabbitListener(queues = "${spring.rabbitmq.ocr-status-queue}")
    public void listen(@Payload CaptureOCRStatusRequest request) {
        log.info("Message received from queue for Capture ID: {}", request.captureId());
        log.debug("Full payload received: {}", request);
        useCase.execute(mapper.toOCRStatus(request));
    }
}
