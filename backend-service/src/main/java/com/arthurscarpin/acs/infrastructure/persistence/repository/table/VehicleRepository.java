package com.arthurscarpin.acs.infrastructure.persistence.repository.table;

import com.arthurscarpin.acs.infrastructure.persistence.entity.table.VehicleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, UUID> {

    @EntityGraph(attributePaths = {"owner"})
    Optional<VehicleEntity> findByPlate(String plate);

    @EntityGraph(attributePaths = "owner")
    Optional<VehicleEntity> findById(UUID id);

    @EntityGraph(attributePaths = "owner")
    Page<VehicleEntity> findAll(Pageable pageable);
}
