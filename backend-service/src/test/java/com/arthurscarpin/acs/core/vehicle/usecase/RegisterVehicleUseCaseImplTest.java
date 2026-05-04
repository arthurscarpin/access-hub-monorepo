package com.arthurscarpin.acs.core.vehicle.usecase;

import com.arthurscarpin.acs.core.owner.domain.DocumentType;
import com.arthurscarpin.acs.core.owner.domain.Owner;
import com.arthurscarpin.acs.core.owner.exception.OwnerNotFoundException;
import com.arthurscarpin.acs.core.owner.gateway.OwnerGateway;
import com.arthurscarpin.acs.core.vehicle.domain.Plate;
import com.arthurscarpin.acs.core.vehicle.domain.Vehicle;
import com.arthurscarpin.acs.core.vehicle.exception.PlateDuplicateException;
import com.arthurscarpin.acs.core.vehicle.gateway.VehicleGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterVehicleUseCaseImplTest {

    @InjectMocks
    private RegisterVehicleUseCaseImpl useCase;

    @Mock
    private VehicleGateway vehicleGateway;

    @Mock
    private OwnerGateway ownerGateway;

    @Test
    @DisplayName("Given valid vehicle and existing owner, when registering, then should save vehicle successfully")
    void shouldRegisterVehicleSuccessfully() {
        Plate plate = new Plate("BRA1S23");
        Vehicle vehicle = Vehicle.create(plate, "Audi A8", UUID.randomUUID());
        Owner owner = new Owner(
                null,
                "Maria Oliveira",
                "111.444.777-35",
                DocumentType.CPF,
                "contact@example.com"
        );

        when(vehicleGateway.findByPlate(plate.plate())).thenReturn(Optional.empty());
        when(ownerGateway.findById(vehicle.ownerId())).thenReturn(Optional.of(owner));
        when(vehicleGateway.save(any())).thenAnswer(inv -> inv.getArgument(0));
        Vehicle response = useCase.execute(vehicle);

        assertEquals(vehicle.model(), response.model());
        assertEquals(owner.id(), response.ownerId());
        verify(vehicleGateway).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Given existing plate, when registering vehicle, then should throw PlateDuplicateException")
    void shouldThrowExceptionWhenPlateAlreadyExists() {
        Plate plate = new Plate("BRA1S23");
        Vehicle vehicle = Vehicle.create(plate, "Audi A8", UUID.randomUUID());

        when(vehicleGateway.findByPlate(plate.plate())).thenReturn(Optional.of(vehicle));

        assertThrows(PlateDuplicateException.class, () -> useCase.execute(vehicle));
        verify(ownerGateway, never()).findById(any());
        verify(vehicleGateway, never()).save(any());
    }

    @Test
    @DisplayName("Given non-existent owner, when registering vehicle, then should throw OwnerNotFoundException")
    void shouldThrowExceptionWhenOwnerNotFound() {
        Plate plate = new Plate("BRA1S23");
        Vehicle vehicle = Vehicle.create(plate, "Audi A8", UUID.randomUUID());

        when(vehicleGateway.findByPlate(plate.plate())).thenReturn(Optional.empty());
        when(ownerGateway.findById(vehicle.ownerId())).thenReturn(Optional.empty());

        assertThrows(OwnerNotFoundException.class, () -> useCase.execute(vehicle));
        verify(vehicleGateway, never()).save(any());
    }

    @Test
    @DisplayName("Given valid vehicle data, when registering, then should create vehicle using factory method with correct data")
    void shouldCreateVehicleUsingFactory() {
        Plate plate = new Plate("BRA1S23");
        Vehicle vehicle = Vehicle.create(plate, "Audi A8", UUID.randomUUID());
        Owner owner = new Owner(
                vehicle.ownerId(),
                "Arthur",
                "123",
                DocumentType.CPF,
                "mail@test.com"
        );

        when(vehicleGateway.findByPlate(any())).thenReturn(Optional.empty());
        when(ownerGateway.findById(any())).thenReturn(Optional.of(owner));
        when(vehicleGateway.save(any())).thenAnswer(inv -> inv.getArgument(0));
        Vehicle result = useCase.execute(vehicle);

        assertNotNull(result);
        assertEquals(vehicle.model(), result.model());
        assertEquals(owner.id(), result.ownerId());
    }

    @Test
    @DisplayName("Given invalid vehicle due to duplicate plate, when registering, then should not persist vehicle")
    void shouldNotSaveVehicleWhenValidationFails() {
        Plate plate = new Plate("BRA1S23");
        Vehicle vehicle = Vehicle.create(plate, "Audi A8", UUID.randomUUID());

        when(vehicleGateway.findByPlate(any())).thenReturn(Optional.of(vehicle));

        assertThrows(PlateDuplicateException.class, () -> useCase.execute(vehicle));
        verify(vehicleGateway, never()).save(any());
    }
}