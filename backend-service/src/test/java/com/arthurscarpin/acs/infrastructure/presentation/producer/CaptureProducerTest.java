package com.arthurscarpin.acs.infrastructure.presentation.producer;

import com.arthurscarpin.acs.AccessControlSystemIntegrationTest;
import com.arthurscarpin.acs.core.capture.domain.CaptureMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class CaptureProducerTest extends AccessControlSystemIntegrationTest {

    @Autowired
    private CaptureProducer producer;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    @Qualifier("ocrProcessingQueue")
    private Queue queue;

    @BeforeEach
    void setup() {
        rabbitTemplate.execute(channel -> {
            channel.queuePurge(queue.getName());
            return null;
        });
    }

    @Test
    @DisplayName("Given valid CaptureMessage When publishing Then message should be delivered to RabbitMQ queue")
    void shouldPublishMessageSuccessfully() {

        CaptureMessage message = new CaptureMessage(
                "capture-123",
                "image-456",
                "plate.jpg",
                Instant.parse("2026-01-01T00:00:00Z")
        );

        producer.publish(message);

        Object received = rabbitTemplate.receiveAndConvert(queue.getName(), 5000);

        assertNotNull(received);
    }

    @Test
    @DisplayName("Given CaptureMessage When publishing Then should preserve all fields correctly")
    void shouldPreserveAllMessageFields() {

        CaptureMessage message = new CaptureMessage(
                "capture-999",
                "image-111",
                "car.jpg",
                Instant.parse("2026-01-01T10:15:30Z")
        );

        producer.publish(message);

        CaptureMessage received =
                (CaptureMessage) rabbitTemplate.receiveAndConvert(queue.getName(), 5000);

        assertNotNull(received);
        assertEquals("capture-999", received.captureId());
        assertEquals("image-111", received.imageId());
        assertEquals("car.jpg", received.filename());
        assertEquals(Instant.parse("2026-01-01T10:15:30Z"), received.timestamp());
    }

    @Test
    @DisplayName("Given no message published When consuming Then should return null")
    void shouldReturnNullWhenQueueIsEmpty() {

        Object received = rabbitTemplate.receiveAndConvert(queue.getName(), 2000);

        assertNull(received);
    }

    @Test
    @DisplayName("Given multiple CaptureMessages When publishing Then all should be delivered")
    void shouldPublishMultipleMessages() {

        producer.publish(new CaptureMessage("c1", "i1", "a.jpg", Instant.now()));
        producer.publish(new CaptureMessage("c2", "i2", "b.jpg", Instant.now()));

        Object first = rabbitTemplate.receiveAndConvert(queue.getName(), 2000);
        Object second = rabbitTemplate.receiveAndConvert(queue.getName(), 2000);

        assertNotNull(first);
        assertNotNull(second);
    }
}