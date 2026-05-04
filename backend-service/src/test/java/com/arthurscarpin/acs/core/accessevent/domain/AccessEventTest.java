package com.arthurscarpin.acs.core.accessevent.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccessEventTest {

    @Test
    @DisplayName("Given valid data, when creating AccessEvent, then should set id as null")
    void shouldCreateAccessEventWithNullId() {
        String plate = "BRA1S23";
        OffsetDateTime timestamp = OffsetDateTime.now();

        AccessEvent event = AccessEvent.create(
                plate,
                timestamp,
                Direction.IN,
                AccessResult.AUTHORIZED
        );

        assertNull(event.id());
    }

    @Test
    @DisplayName("Given valid AccessEvent data, when creating, then should preserve all fields")
    void shouldPreserveAllFieldsOnCreation() {
        String plate = "BRA1S23";
        OffsetDateTime timestamp = OffsetDateTime.now();
        Direction direction = Direction.OUT;
        AccessResult result = AccessResult.DENIED;

        AccessEvent event = AccessEvent.create(plate, timestamp, direction, result);

        assertAll(
                () -> assertEquals(plate, event.plate()),
                () -> assertEquals(timestamp, event.timestamp()),
                () -> assertEquals(direction, event.direction()),
                () -> assertEquals(result, event.result())
        );
    }

    @Test
    @DisplayName("Given two identical AccessEvents, when comparing, then should be equal")
    void shouldBeEqualWhenAllFieldsIncludingIdAreSame() {
        UUID id = UUID.randomUUID();
        OffsetDateTime timestamp = OffsetDateTime.now();

        AccessEvent event1 = new AccessEvent(
                id,
                "BRA1S23",
                timestamp,
                Direction.IN,
                AccessResult.AUTHORIZED
        );
        AccessEvent event2 = new AccessEvent(
                id,
                "BRA1S23",
                timestamp,
                Direction.IN,
                AccessResult.AUTHORIZED
        );

        assertEquals(event1, event2);
    }

    @Test
    @DisplayName("Given AccessEvents with different plate, when comparing, then should not be equal")
    void shouldNotBeEqualWhenPlateIsDifferent() {
        UUID id = UUID.randomUUID();
        OffsetDateTime timestamp = OffsetDateTime.now();

        AccessEvent event1 = new AccessEvent(
                id,
                "BRA1S23",
                timestamp,
                Direction.IN,
                AccessResult.AUTHORIZED
        );
        AccessEvent event2 = new AccessEvent(
                id,
                "BRA1S24",
                timestamp,
                Direction.IN,
                AccessResult.AUTHORIZED
        );

        assertNotEquals(event1, event2);
    }

    @Test
    @DisplayName("Given AccessEvents with different result, when comparing, then should not be equal")
    void shouldNotBeEqualWhenResultIsDifferent() {
        UUID id = UUID.randomUUID();
        OffsetDateTime timestamp = OffsetDateTime.now();

        AccessEvent event1 = new AccessEvent(
                id,
                "BRA1S23",
                timestamp,
                Direction.IN,
                AccessResult.AUTHORIZED
        );

        AccessEvent event2 = new AccessEvent(
                id,
                "BRA1S23",
                timestamp,
                Direction.IN,
                AccessResult.DENIED
        );

        assertNotEquals(event1, event2);
    }
}