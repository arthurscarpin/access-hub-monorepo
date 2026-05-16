package com.arthurscarpin.acs.infrastructure.presentation.consumer;

import com.arthurscarpin.acs.core.capture.usecase.ResultCaptureUseCase;
import com.arthurscarpin.acs.infrastructure.presentation.request.PlateRecognizedRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlateRecognizedConsumer {

    private final ResultCaptureUseCase useCase;

    @RabbitListener(queues = "${spring.rabbitmq.ai-result-queue}")
    public void listen(@Payload PlateRecognizedRequest request) {
        log.info("Message received from queue for Capture ID: {}", request.capture().id());
        log.debug("Full payload received: {}", request);
        useCase.execute(request.capture());
    }
}
