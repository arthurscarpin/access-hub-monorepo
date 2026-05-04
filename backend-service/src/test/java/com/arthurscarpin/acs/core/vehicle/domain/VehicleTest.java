package com.arthurscarpin.acs.core.vehicle.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    @Test
    @DisplayName("Given valid data, when creating Vehicle, then status should be ACTIVE by default")
    void shouldCreateVehicleWithActiveStatus() {
        Plate plate = new Plate("ABC-1234");
        UUID ownerId = UUID.randomUUID();

        Vehicle vehicle = Vehicle.create(plate, "Civic", ownerId);

        assertNull(vehicle.id());
        assertEquals("ABC1234", vehicle.plate());
        assertEquals("Civic", vehicle.model());
        assertEquals(VehicleStatus.ACTIVE, vehicle.status());
        assertEquals(ownerId, vehicle.ownerId());
    }

    @Test
    @DisplayName("Given vehicle, when changing status, then should return new instance with updated status")
    void shouldChangeStatusAndReturnNewInstance() {
        Vehicle vehicle = Vehicle.create(
                new Plate("ABC-1234"),
                "Civic",
                UUID.randomUUID()
        );

        Vehicle updated = vehicle.changeStatus(VehicleStatus.BLOCKED);

        assertNotEquals(vehicle, updated);
        assertEquals(VehicleStatus.BLOCKED, updated.status());
        assertEquals(VehicleStatus.ACTIVE, vehicle.status());
    }

    @Test
    @DisplayName("Given vehicle, when changing status, then other fields should remain unchanged")
    void shouldPreserveFieldsWhenChangingStatus() {
        UUID ownerId = UUID.randomUUID();

        Vehicle vehicle = Vehicle.create(
                new Plate("ABC-1234"),
                "Civic",
                ownerId
        );

        Vehicle updated = vehicle.changeStatus(VehicleStatus.BLOCKED);

        assertEquals(vehicle.plate(), updated.plate());
        assertEquals(vehicle.model(), updated.model());
        assertEquals(vehicle.ownerId(), updated.ownerId());
        assertEquals(vehicle.id(), updated.id());
    }
}