package com.arthurscarpin.acs.infrastructure.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    // OCR Configuration
    @Value("${spring.rabbitmq.ocr-queue}")
    private String ocrQueue;
    @Value("${spring.rabbitmq.ocr-routing-key}")
    private String ocrRoutingKey;

    // Status Configuration
    @Value("${spring.rabbitmq.ocr-status-queue}")
    private String ocrStatusQueue;
    @Value("${spring.rabbitmq.ocr-status-routing-key}")
    private String ocrStatusRoutingKey;

    // AI Validation Configuration
    @Value("${spring.rabbitmq.ai-validation-queue}")
    private String aiValidationQueue;
    @Value("${spring.rabbitmq.ai-validation-routing-key}")
    private String aiValidationRoutingKey;

    // AI Results Configuration
    @Value("${spring.rabbitmq.ai-result-queue}")
    private String aiResultQueue;
    @Value("${spring.rabbitmq.ai-result-routing-key}")
    private String aiResultRoutingKey;

    // Core Infrastructure Beans
    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public TopicExchange captureTopicExchange() {
        return ExchangeBuilder
                .topicExchange(exchange)
                .durable(true)
                .build();
    }

    // Queues Declaration
    @Bean
    public Queue ocrProcessingQueue() {
        return QueueBuilder.durable(ocrQueue).build();
    }

    @Bean
    public Queue ocrStatusQueue() {
        return QueueBuilder.durable(ocrStatusQueue).build();
    }

    @Bean
    public Queue aiValidationQueue() {
        return QueueBuilder.durable(aiValidationQueue).build();
    }

    @Bean
    public Queue aiResultQueue() {
        return QueueBuilder.durable(aiResultQueue).build();
    }

    // Bindings (Connecting Queues to the Topic Exchange)
    @Bean
    public Binding ocrProcessingBinding(TopicExchange captureEventsExchange) {
        return BindingBuilder
                .bind(ocrProcessingQueue())
                .to(captureEventsExchange)
                .with(ocrRoutingKey);
    }

    @Bean
    public Binding ocrStatusBinding(TopicExchange captureEventsExchange) {
        return BindingBuilder
                .bind(ocrStatusQueue())
                .to(captureEventsExchange)
                .with(ocrStatusRoutingKey);
    }

    @Bean
    public Binding aiValidationBinding(TopicExchange captureEventsExchange) {
        return BindingBuilder
                .bind(aiValidationQueue())
                .to(captureEventsExchange)
                .with(aiValidationRoutingKey);
    }

    @Bean
    public Binding aiResultBinding(TopicExchange captureEventsExchange) {
        return BindingBuilder
                .bind(aiResultQueue())
                .to(captureEventsExchange)
                .with(aiResultRoutingKey);
    }
}