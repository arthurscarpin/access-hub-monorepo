package com.arthurscarpin.acs.infrastructure.presentation.producer;

import com.arthurscarpin.acs.core.capture.domain.Capture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AIProducer {

    private final RabbitTemplate template;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.ai-validation-routing-key}")
    private String routingKey;

    public void publish(Capture message) {
        log.info("Publishing capture agent message to message queue");
        log.debug("Exchange: {} | Routing Key: {} | Message ID: {}", exchange, routingKey, message.id());
        template.convertAndSend(exchange, routingKey, message);
        log.info("Agent capture message published successfully | Capture ID: {} | Destination: {}/{}",
                message.id(), exchange, routingKey);
    }
}
