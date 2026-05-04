package com.arthurscarpin.acs.core.vehicle.usecase;

import com.arthurscarpin.acs.core.vehicle.domain.Vehicle;

import java.util.UUID;

public interface UpdateVehicleStatusUseCase {

    Vehicle execute(UUID id);
}
