package com.arthurscarpin.acs.infrastructure.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.queue}")
    private String queue;

    @Value("${spring.rabbitmq.dead-exchange}")
    private String deadExchange;

    @Value("${spring.rabbitmq.dead-queue}")
    private String deadQueue;

    @Value("${spring.rabbitmq.routing-key}")
    private String routingKey;

    @Value("${spring.rabbitmq.dead-routing-key}")
    private String deadRoutingKey;

    @Bean
    public ApplicationRunner rabbitInitializer(RabbitAdmin rabbitAdmin) {
        return args -> rabbitAdmin.initialize();
    }

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            JacksonJsonMessageConverter messageConverter
    ) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean(name = "captureEventsExchange")
    public TopicExchange captureEventsExchange() {
        return ExchangeBuilder
                .topicExchange(exchange)
                .durable(true)
                .build();
    }

    @Bean(name = "deadLetterExchange")
    public TopicExchange deadLetterExchange() {
        return ExchangeBuilder
                .topicExchange(deadExchange)
                .durable(true)
                .build();
    }

    @Bean(name = "ocrProcessingQueue")
    public Queue ocrProcessingQueue() {
        return QueueBuilder
                .durable(queue)
                .deadLetterExchange(deadExchange)
                .deadLetterRoutingKey(deadRoutingKey)
                .build();
    }

    @Bean(name = "deadLetterQueue")
    public Queue deadLetterQueue() {
        return QueueBuilder
                .durable(deadQueue)
                .build();
    }

    @Bean
    public Binding ocrProcessingBinding(
            @Qualifier("ocrProcessingQueue")
            Queue ocrProcessingQueue,
            @Qualifier("captureEventsExchange")
            TopicExchange captureEventsExchange
    ) {
        return BindingBuilder
                .bind(ocrProcessingQueue)
                .to(captureEventsExchange)
                .with(routingKey);
    }

    @Bean
    public Binding deadLetterBinding(
            @Qualifier("deadLetterQueue")
            Queue deadLetterQueue,
            @Qualifier("deadLetterExchange")
            TopicExchange deadLetterExchange
    ) {
        return BindingBuilder
                .bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with(deadRoutingKey);
    }
}