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

    // Dead Letter Exchange name
    @Value("${spring.rabbitmq.exchange}.dlx")
    private String deadLetterExchange;

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

    // Dead Letter Exchange
    @Bean
    public DirectExchange deadLetterExchange() {
        return ExchangeBuilder
                .directExchange(deadLetterExchange)
                .durable(true)
                .build();
    }

    // Dead Letter Queues
    @Bean
    public Queue ocrProcessingDlq() {
        return QueueBuilder.durable(ocrQueue + ".dlq").build();
    }

    @Bean
    public Queue ocrStatusDlq() {
        return QueueBuilder.durable(ocrStatusQueue + ".dlq").build();
    }

    @Bean
    public Queue aiValidationDlq() {
        return QueueBuilder.durable(aiValidationQueue + ".dlq").build();
    }

    @Bean
    public Queue aiResultDlq() {
        return QueueBuilder.durable(aiResultQueue + ".dlq").build();
    }

    // DLQs Bindings on DLX
    @Bean
    public Binding ocrProcessingDlqBinding() {
        return BindingBuilder
                .bind(ocrProcessingDlq())
                .to(deadLetterExchange())
                .with(ocrQueue + ".dlq");
    }

    @Bean
    public Binding ocrStatusDlqBinding() {
        return BindingBuilder
                .bind(ocrStatusDlq())
                .to(deadLetterExchange())
                .with(ocrStatusQueue + ".dlq");
    }

    @Bean
    public Binding aiValidationDlqBinding() {
        return BindingBuilder
                .bind(aiValidationDlq())
                .to(deadLetterExchange())
                .with(aiValidationQueue + ".dlq");
    }

    @Bean
    public Binding aiResultDlqBinding() {
        return BindingBuilder
                .bind(aiResultDlq())
                .to(deadLetterExchange())
                .with(aiResultQueue + ".dlq");
    }

    // Queues
    @Bean
    public Queue ocrProcessingQueue() {
        return QueueBuilder.durable(ocrQueue)
                .withArgument("x-dead-letter-exchange", deadLetterExchange)
                .withArgument("x-dead-letter-routing-key", ocrQueue + ".dlq")
                .build();
    }

    @Bean
    public Queue ocrStatusQueue() {
        return QueueBuilder.durable(ocrStatusQueue)
                .withArgument("x-dead-letter-exchange", deadLetterExchange)
                .withArgument("x-dead-letter-routing-key", ocrStatusQueue + ".dlq")
                .build();
    }

    @Bean
    public Queue aiValidationQueue() {
        return QueueBuilder.durable(aiValidationQueue)
                .withArgument("x-dead-letter-exchange", deadLetterExchange)
                .withArgument("x-dead-letter-routing-key", aiValidationQueue + ".dlq")
                .build();
    }

    @Bean
    public Queue aiResultQueue() {
        return QueueBuilder.durable(aiResultQueue)
                .withArgument("x-dead-letter-exchange", deadLetterExchange)
                .withArgument("x-dead-letter-routing-key", aiResultQueue + ".dlq")
                .build();
    }

    // Bindings
    @Bean
    public Binding ocrProcessingBinding(TopicExchange captureTopicExchange) {
        return BindingBuilder
                .bind(ocrProcessingQueue())
                .to(captureTopicExchange)
                .with(ocrRoutingKey);
    }

    @Bean
    public Binding ocrStatusBinding(TopicExchange captureTopicExchange) {
        return BindingBuilder
                .bind(ocrStatusQueue())
                .to(captureTopicExchange)
                .with(ocrStatusRoutingKey);
    }

    @Bean
    public Binding aiValidationBinding(TopicExchange captureTopicExchange) {
        return BindingBuilder
                .bind(aiValidationQueue())
                .to(captureTopicExchange)
                .with(aiValidationRoutingKey);
    }

    @Bean
    public Binding aiResultBinding(TopicExchange captureTopicExchange) {
        return BindingBuilder
                .bind(aiResultQueue())
                .to(captureTopicExchange)
                .with(aiResultRoutingKey);
    }
}