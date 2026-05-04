package com.arthurscarpin.acs.core.vehicle.domain;

import java.util.UUID;

public record Vehicle(
        UUID id,
        String plate,
        String model,
        VehicleStatus status,
        UUID ownerId
) {
    public Vehicle changeStatus(VehicleStatus newStatus) {
        return new Vehicle(id, plate, model, newStatus, ownerId);
    }

    public static Vehicle create(Plate plate, String model, UUID ownerId) {
        return new Vehicle(null, plate.plate(), model, VehicleStatus.ACTIVE, ownerId);
    }
}
