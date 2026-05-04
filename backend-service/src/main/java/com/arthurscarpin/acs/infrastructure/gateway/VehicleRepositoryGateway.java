package com.arthurscarpin.acs.infrastructure.gateway;

import com.arthurscarpin.acs.core.owner.exception.OwnerNotFoundException;
import com.arthurscarpin.acs.core.vehicle.domain.Vehicle;
import com.arthurscarpin.acs.core.vehicle.gateway.VehicleGateway;
import com.arthurscarpin.acs.infrastructure.mapper.VehicleMapper;
import com.arthurscarpin.acs.infrastructure.persistence.entity.OwnerEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.VehicleEntity;
import com.arthurscarpin.acs.infrastructure.persistence.repository.OwnerRepository;
import com.arthurscarpin.acs.infrastructure.persistence.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VehicleRepositoryGateway implements VehicleGateway {

    private final VehicleRepository vehicleRepository;

    private final OwnerRepository ownerRepository;

    private final VehicleMapper vehicleMapper;

    @Override
    public Optional<Vehicle> findById(UUID id) {
        return vehicleRepository.findById(id)
                .map(vehicleMapper::fromEntityToDomain);
    }

    @Override
    public Optional<Vehicle> findByPlate(String plate) {
        return vehicleRepository.findByPlate(plate);
    }

    @Transactional
    @Override
    public Vehicle save(Vehicle vehicleDomain) {
        VehicleEntity vehicleEntity = vehicleMapper.fromDomainToEntity(vehicleDomain);
        OwnerEntity ownerEntity = ownerRepository.findById(vehicleDomain.ownerId())
                .orElseThrow(() -> new OwnerNotFoundException("Owner not found"));

        vehicleEntity.setOwner(ownerEntity);
        return vehicleMapper.fromEntityToDomain(vehicleRepository.save(vehicleEntity));
    }
}
