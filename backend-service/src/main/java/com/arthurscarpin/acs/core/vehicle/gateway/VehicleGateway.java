package com.arthurscarpin.acs.core.vehicle.gateway;

import com.arthurscarpin.acs.core.vehicle.domain.Vehicle;

import java.util.Optional;
import java.util.UUID;

public interface VehicleGateway {

    Optional<Vehicle> findById(UUID id);

    Optional<Vehicle> findByPlate(String plate);

    Vehicle save(Vehicle vehicle);
}
