package com.arthurscarpin.acs.infrastructure.presentation.producer;

import com.arthurscarpin.acs.core.capture.domain.CaptureMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CaptureProducer {

    private final RabbitTemplate template;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routing-key}")
    private String routingKey;

    public void publish(CaptureMessage message) {
        log.info("Publishing capture message to message queue");
        log.debug("Exchange: {} | Routing Key: {} | Message ID: {}", exchange, routingKey, message.captureId());
        template.convertAndSend(exchange, routingKey, message);
        log.info("Capture message published successfully | Capture ID: {} | Destination: {}/{}",
                message.captureId(), exchange, routingKey);
    }
}
