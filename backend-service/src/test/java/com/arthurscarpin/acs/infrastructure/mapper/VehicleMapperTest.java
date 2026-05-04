package com.arthurscarpin.acs.infrastructure.mapper;

import com.arthurscarpin.acs.core.vehicle.domain.Vehicle;
import com.arthurscarpin.acs.core.vehicle.domain.VehicleStatus;
import com.arthurscarpin.acs.infrastructure.persistence.entity.OwnerEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.VehicleEntity;
import com.arthurscarpin.acs.infrastructure.presentation.request.VehicleRequest;
import com.arthurscarpin.acs.infrastructure.presentation.response.VehicleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VehicleMapperTest {

    private final VehicleMapper mapper = Mappers.getMapper(VehicleMapper.class);

    @Test
    @DisplayName("Given VehicleRequest When mapping to domain Then should map correctly")
    void shouldMapVehicleRequestToDomain() {
        UUID ownerId = UUID.randomUUID();

        VehicleRequest request = new VehicleRequest(
                "BRA1S23",
                "Audi A8",
                ownerId
        );

        Vehicle result = mapper.fromRequestToDomain(request);

        assertNotNull(result);
        assertNull(result.id());
        assertEquals(request.plate(), result.plate());
        assertEquals(request.model(), result.model());
        assertEquals(request.ownerId(), result.ownerId());
    }

    @Test
    @DisplayName("Given Vehicle domain When mapping to response Then should map correctly")
    void shouldMapVehicleDomainToResponse() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Vehicle vehicle = new Vehicle(
                id,
                "BRA1S23",
                "Audi A8",
                VehicleStatus.ACTIVE,
                ownerId
        );

        VehicleResponse response = mapper.fromDomainToResponse(vehicle);

        assertNotNull(response);
        assertEquals(vehicle.id(), response.id());
        assertEquals(vehicle.plate(), response.plate());
        assertEquals(vehicle.model(), response.model());
        assertEquals(vehicle.status(), response.status());
        assertEquals(vehicle.ownerId(), response.ownerId());
    }

    @Test
    @DisplayName("Given Vehicle domain When mapping to entity Then should map correctly")
    void shouldMapVehicleDomainToEntity() {
        UUID ownerId = UUID.randomUUID();

        Vehicle vehicle = new Vehicle(
                UUID.randomUUID(),
                "BRA1S23",
                "Audi A8",
                VehicleStatus.ACTIVE,
                ownerId
        );

        VehicleEntity entity = mapper.fromDomainToEntity(vehicle);

        assertNotNull(entity);
        assertEquals(vehicle.id(), entity.getId());
        assertEquals(vehicle.plate(), entity.getPlate());
        assertEquals(vehicle.model(), entity.getModel());
        assertEquals(vehicle.status(), entity.getStatus());
        assertNotNull(entity.getOwner());
        assertEquals(ownerId, entity.getOwner().getId());
    }

    @Test
    @DisplayName("Given VehicleEntity When mapping to domain Then should map ownerId correctly")
    void shouldMapVehicleEntityToDomain() {
        UUID ownerId = UUID.randomUUID();

        OwnerEntity owner = new OwnerEntity();
        owner.setId(ownerId);

        VehicleEntity entity = VehicleEntity.builder()
                .id(UUID.randomUUID())
                .plate("BRA1S23")
                .model("Audi A8")
                .status(VehicleStatus.ACTIVE)
                .owner(owner)
                .build();

        Vehicle result = mapper.fromEntityToDomain(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.id());
        assertEquals(entity.getPlate(), result.plate());
        assertEquals(entity.getModel(), result.model());
        assertEquals(entity.getStatus(), result.status());
        assertEquals(ownerId, result.ownerId());
    }

    @Test
    @DisplayName("Given null values When mapping Then should return null safely")
    void shouldReturnNullWhenMappingNullValues() {
        assertNull(mapper.fromRequestToDomain(null));
        assertNull(mapper.fromDomainToResponse(null));
        assertNull(mapper.fromDomainToEntity(null));
        assertNull(mapper.fromEntityToDomain(null));
    }
}