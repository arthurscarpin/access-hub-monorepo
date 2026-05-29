package com.arthurscarpin.acs.core.vehicle.usecase;

import com.arthurscarpin.acs.core.pagination.PageInput;
import com.arthurscarpin.acs.core.pagination.PageOutput;
import com.arthurscarpin.acs.core.vehicle.domain.Vehicle;
import com.arthurscarpin.acs.core.vehicle.gateway.VehicleGateway;

public class GetVehiclesUseCaseImpl implements GetVehiclesUseCase {

    private final VehicleGateway gateway;

    public GetVehiclesUseCaseImpl(VehicleGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public PageOutput<Vehicle> execute(PageInput pageInput) {
        return gateway.findByFilters(pageInput);
    }
}
