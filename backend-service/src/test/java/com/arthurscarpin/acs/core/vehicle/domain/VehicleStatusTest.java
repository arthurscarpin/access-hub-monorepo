package com.arthurscarpin.acs.core.vehicle.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleStatusTest {

    @Test
    @DisplayName("Given ACTIVE status, when toggle, then should return BLOCKED")
    void shouldToggleActiveToBlocked() {
        VehicleStatus status = VehicleStatus.ACTIVE;

        VehicleStatus result = status.toggle();

        assertEquals(VehicleStatus.BLOCKED, result);
    }

    @Test
    @DisplayName("Given BLOCKED status, when toggle, then should return ACTIVE")
    void shouldToggleBlockedToActive() {
        VehicleStatus status = VehicleStatus.BLOCKED;

        VehicleStatus result = status.toggle();

        assertEquals(VehicleStatus.ACTIVE, result);
    }

    @Test
    @DisplayName("Given status toggled twice, when toggling again, then should return original value")
    void shouldToggleTwiceReturnOriginal() {
        VehicleStatus status = VehicleStatus.ACTIVE;

        VehicleStatus result = status.toggle().toggle();

        assertEquals(VehicleStatus.ACTIVE, result);
    }
}