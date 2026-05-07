package com.arthurscarpin.acs.infrastructure.mapper;

import com.arthurscarpin.acs.core.vehicle.domain.Vehicle;
import com.arthurscarpin.acs.infrastructure.persistence.entity.table.OwnerEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.table.VehicleEntity;
import com.arthurscarpin.acs.infrastructure.presentation.request.VehicleRequest;
import com.arthurscarpin.acs.infrastructure.presentation.response.VehicleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    Vehicle fromRequestToDomain(VehicleRequest request);

    VehicleResponse fromDomainToResponse(Vehicle vehicle);

    @Mapping(target = "owner", source = "ownerId")
    VehicleEntity fromDomainToEntity(Vehicle vehicle);

    @Mapping(target = "ownerId", source = "owner")
    Vehicle fromEntityToDomain(VehicleEntity entity);

    default OwnerEntity map(UUID ownerId) {
        if (ownerId == null) return null;
        OwnerEntity owner = new OwnerEntity();
        owner.setId(ownerId);
        return owner;
    }

    default UUID mapOwnerToId(OwnerEntity owner) {
        return owner != null ? owner.getId() : null;
    }
}
