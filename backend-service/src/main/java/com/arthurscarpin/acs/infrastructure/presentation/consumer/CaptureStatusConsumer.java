package com.arthurscarpin.acs.infrastructure.presentation.consumer;

import com.arthurscarpin.acs.core.capture.usecase.StatusCaptureUseCase;
import com.arthurscarpin.acs.infrastructure.mapper.CaptureMapper;
import com.arthurscarpin.acs.infrastructure.presentation.request.CaptureOCRStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CaptureStatusConsumer {

    private final StatusCaptureUseCase useCase;

    private final CaptureMapper mapper;

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void listen(@Payload CaptureOCRStatusRequest request) {
        System.out.println("Received Capture Status Request");
        useCase.execute(mapper.toOCRStatus(request));
    }
}
