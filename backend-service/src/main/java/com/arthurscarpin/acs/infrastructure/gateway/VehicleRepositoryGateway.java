package com.arthurscarpin.acs.infrastructure.gateway;

import com.arthurscarpin.acs.core.owner.exception.OwnerNotFoundException;
import com.arthurscarpin.acs.core.pagination.PageInput;
import com.arthurscarpin.acs.core.pagination.PageOutput;
import com.arthurscarpin.acs.core.vehicle.domain.Vehicle;
import com.arthurscarpin.acs.core.vehicle.gateway.VehicleGateway;
import com.arthurscarpin.acs.infrastructure.mapper.VehicleMapper;
import com.arthurscarpin.acs.infrastructure.persistence.entity.table.OwnerEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.table.VehicleEntity;
import com.arthurscarpin.acs.infrastructure.persistence.repository.table.OwnerRepository;
import com.arthurscarpin.acs.infrastructure.persistence.repository.table.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
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

    @Override
    public PageOutput<Vehicle> findByFilters(PageInput pageInput) {
        Pageable pageable = PageRequest.of(
                pageInput.pageNumber(),
                pageInput.pageSize(),
                Sort.by(Sort.Direction.ASC, "model")
        );

        Page<VehicleEntity> page = vehicleRepository.findAll(pageable);

        List<Vehicle> content = page.getContent().stream()
                .map(vehicleMapper::fromEntityToDomain)
                .toList();

        return new PageOutput<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
