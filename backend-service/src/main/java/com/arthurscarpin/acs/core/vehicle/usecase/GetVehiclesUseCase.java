package com.arthurscarpin.acs.core.vehicle.usecase;

import com.arthurscarpin.acs.core.pagination.PageInput;
import com.arthurscarpin.acs.core.pagination.PageOutput;
import com.arthurscarpin.acs.core.vehicle.domain.Vehicle;

public interface GetVehiclesUseCase {

    PageOutput<Vehicle> execute(PageInput pageInput);
}
