package com.arthurscarpin.acs.core.vehicle.usecase;

import com.arthurscarpin.acs.core.vehicle.domain.Vehicle;
import com.arthurscarpin.acs.core.vehicle.domain.VehicleStatus;
import com.arthurscarpin.acs.core.vehicle.exception.VehicleNotFoundException;
import com.arthurscarpin.acs.core.vehicle.gateway.VehicleGateway;

import java.util.UUID;

public class UpdateVehicleStatusUseCaseImpl implements UpdateVehicleStatusUseCase{

    private final VehicleGateway gateway;

    public UpdateVehicleStatusUseCaseImpl(VehicleGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Vehicle execute(UUID id) {
        Vehicle vehicle = gateway.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle with id " + id + " not found"));
        VehicleStatus newStatus = vehicle.status().toggle();
        Vehicle updatedVehicle = vehicle.changeStatus(newStatus);
        return gateway.save(updatedVehicle);
    }
}
