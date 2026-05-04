package com.arthurscarpin.acs.infrastructure.mapper;

import com.arthurscarpin.acs.core.vehicle.domain.Vehicle;
import com.arthurscarpin.acs.infrastructure.persistence.entity.OwnerEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.VehicleEntity;
import com.arthurscarpin.acs.infrastructure.presentation.request.VehicleRequest;
import com.arthurscarpin.acs.infrastructure.presentation.response.VehicleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    Vehicle fromRequestToDomain(VehicleRequest request);

    VehicleResponse fromDomainToResponse(Vehicle vehicle);

    VehicleEntity fromDomainToEntity(Vehicle vehicle);

    @Mapping(target = "ownerId", source = "owner")
    Vehicle fromEntityToDomain(VehicleEntity entity);

    default UUID mapOwnerToId(OwnerEntity owner) {
        return owner != null ? owner.getId() : null;
    }
}
