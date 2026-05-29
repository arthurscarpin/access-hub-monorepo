package com.arthurscarpin.acs.core.vehicle.domain;

import java.util.UUID;

public record Vehicle(
        UUID id,
        String plate,
        String model,
        VehicleStatus status,
        UUID ownerId,
        String ownerName
) {
    public Vehicle changeStatus(VehicleStatus newStatus) {
        return new Vehicle(id, plate, model, newStatus, ownerId, ownerName);
    }

    public static Vehicle create(Plate plate, String model, UUID ownerId, String ownerName) {
        return new Vehicle(null, plate.plate(), model, VehicleStatus.ACTIVE, ownerId, ownerName);
    }
}
