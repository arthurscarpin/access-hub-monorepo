package com.arthurscarpin.acs.core.accessevent.usecase;

import com.arthurscarpin.acs.core.accessevent.domain.AccessEvent;
import com.arthurscarpin.acs.core.accessevent.domain.AccessResult;
import com.arthurscarpin.acs.core.accessevent.gateway.AccessEventGateway;
import com.arthurscarpin.acs.core.vehicle.domain.Plate;
import com.arthurscarpin.acs.core.vehicle.domain.VehicleStatus;
import com.arthurscarpin.acs.core.vehicle.gateway.VehicleGateway;

public class ValidateAccessUseCaseImpl implements ValidateAccessUseCase {

    private final VehicleGateway vehicleGateway;

    private final AccessEventGateway accessEventGateway;

    public ValidateAccessUseCaseImpl(VehicleGateway vehicleGateway, AccessEventGateway accessEventGateway) {
        this.vehicleGateway = vehicleGateway;
        this.accessEventGateway = accessEventGateway;
    }

    @Override
    public AccessEvent execute(AccessEvent accessEvent) {
        Plate plate = new Plate(accessEvent.plate());
        AccessResult accessResult = vehicleGateway.findByPlate(plate.plate())
                .filter(vehicle -> vehicle.status() == VehicleStatus.ACTIVE)
                .map(v -> AccessResult.AUTHORIZED)
                .orElse(AccessResult.DENIED);
        AccessEvent savedAccessEvent = AccessEvent.create(
                plate.plate(),
                accessEvent.timestamp(),
                accessEvent.direction(),
                accessResult
        );
        return accessEventGateway.save(savedAccessEvent);
    }
}
