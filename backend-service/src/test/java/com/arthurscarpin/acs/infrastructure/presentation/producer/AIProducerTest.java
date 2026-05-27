package com.arthurscarpin.acs.infrastructure.presentation.producer;

import com.arthurscarpin.acs.AccessControlSystemIntegrationTest;
import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import com.arthurscarpin.acs.core.capture.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AIProducerTest extends AccessControlSystemIntegrationTest {

    @Autowired
    private AIProducer producer;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    @Qualifier("aiValidationQueue")
    private Queue queue;

    @BeforeEach
    void setup() {
        rabbitTemplate.execute(channel -> {
            channel.queuePurge(queue.getName());
            return null;
        });
    }

    @Test
    @DisplayName("Given valid Capture When publishing Then message should be delivered to AI validation queue")
    void shouldPublishMessageSuccessfully() {
        Capture capture = createMockCapture("capture-123");

        producer.publish(capture);

        Object received = rabbitTemplate.receiveAndConvert(queue.getName(), 5000);

        assertNotNull(received);
        assertInstanceOf(Capture.class, received);
    }

    @Test
    @DisplayName("Given Capture When publishing Then should preserve all domain fields correctly")
    void shouldPreserveAllMessageFields() {
        String expectedId = "capture-unique-id";
        Capture capture = createMockCapture(expectedId);

        producer.publish(capture);

        Capture received = (Capture) rabbitTemplate.receiveAndConvert(queue.getName(), 5000);

        assertNotNull(received);
        assertEquals(expectedId, received.id());
        assertEquals(CaptureStatus.PROCESSING, received.status());
        assertEquals("ABC1234", received.finalPlate());
        assertEquals(1, received.images().size());
        assertEquals("image-1", received.images().get(0).id());
    }

    @Test
    @DisplayName("Given no message published When consuming Then should return null")
    void shouldReturnNullWhenQueueIsEmpty() {
        Object received = rabbitTemplate.receiveAndConvert(queue.getName(), 2000);

        assertNull(received);
    }

    @Test
    @DisplayName("Given multiple Captures When publishing Then all should be delivered to the queue")
    void shouldPublishMultipleMessages() {
        producer.publish(createMockCapture("c1"));
        producer.publish(createMockCapture("c2"));

        Object first = rabbitTemplate.receiveAndConvert(queue.getName(), 2000);
        Object second = rabbitTemplate.receiveAndConvert(queue.getName(), 2000);

        assertNotNull(first);
        assertNotNull(second);
    }

    private Capture createMockCapture(String id) {
        CaptureImage image = new CaptureImage(
                "image-1",
                "plate.jpg",
                ImageStatus.COMPLETED,
                List.of(new CaptureOCR("ABC1234", 0.99, List.of(List.of(0,0)))),
                Instant.now()
        );

        return new Capture(
                id,
                List.of(image),
                CaptureStatus.PROCESSING,
                Direction.IN,
                "ABC1234",
                0.99,
                "AI Analysis",
                Instant.now(),
                Instant.now(),
                Instant.now(),
                1,
                1L
        );
    }
}