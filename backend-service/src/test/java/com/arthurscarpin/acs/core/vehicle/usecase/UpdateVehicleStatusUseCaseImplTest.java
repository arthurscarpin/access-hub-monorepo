package com.arthurscarpin.acs.core.vehicle.usecase;

import com.arthurscarpin.acs.core.vehicle.domain.Plate;
import com.arthurscarpin.acs.core.vehicle.domain.Vehicle;
import com.arthurscarpin.acs.core.vehicle.domain.VehicleStatus;
import com.arthurscarpin.acs.core.vehicle.exception.VehicleNotFoundException;
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
class UpdateVehicleStatusUseCaseImplTest {

    @InjectMocks
    private UpdateVehicleStatusUseCaseImpl useCase;

    @Mock
    private VehicleGateway gateway;

    @Test
    @DisplayName("Given existing vehicle, when toggling status, then should update status successfully")
    void shouldToggleVehicleStatusSuccessfully() {
        UUID id = UUID.randomUUID();
        Plate plate = new Plate("BRA1S23");
        Vehicle vehicleInput = Vehicle.create(plate, "Audi A8", UUID.randomUUID());
        VehicleStatus newStatus = vehicleInput.status().toggle();
        Vehicle vehicleOutput = new Vehicle(
                vehicleInput.id(),
                vehicleInput.plate(),
                vehicleInput.model(),
                newStatus,
                vehicleInput.ownerId()
        );

        when(gateway.findById(id)).thenReturn(Optional.of(vehicleInput));
        when(gateway.save(any())).thenReturn(vehicleOutput);
        Vehicle result = useCase.execute(id);

        assertEquals(newStatus, result.status());
        verify(gateway).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Given non-existent vehicle, when updating status, then should throw VehicleNotFoundException")
    void shouldThrowExceptionWhenVehicleNotFound() {
        UUID id = UUID.randomUUID();

        when(gateway.findById(id)).thenReturn(Optional.empty());

        assertThrows(VehicleNotFoundException.class, () -> useCase.execute(id));
        verify(gateway, never()).save(any());
    }

    @Test
    @DisplayName("Given existing vehicle, when toggling status, then should change status to opposite value")
    void shouldChangeVehicleStatusToOppositeValue() {
        UUID id = UUID.randomUUID();
        Plate plate = new Plate("BRA1S23");
        Vehicle vehicle = Vehicle.create(plate, "Audi A8", UUID.randomUUID());
        VehicleStatus oldStatus = vehicle.status();

        when(gateway.findById(id)).thenReturn(Optional.of(vehicle));
        when(gateway.save(any())).thenAnswer(inv -> inv.getArgument(0));
        Vehicle response = useCase.execute(id);

        assertNotEquals(oldStatus, response.status());
    }

    @Test
    @DisplayName("Given existing vehicle, when updating status, then should use domain method to change status")
    void shouldUseDomainChangeStatusMethod() {
        UUID id = UUID.randomUUID();
        Plate plate = new Plate("BRA1S23");
        Vehicle vehicle = Vehicle.create(plate, "Audi A8", UUID.randomUUID());

        when(gateway.findById(id)).thenReturn(Optional.of(vehicle));
        when(gateway.save(any())).thenAnswer(inv -> inv.getArgument(0));
        Vehicle response = useCase.execute(id);

        assertNotNull(response);
        verify(gateway).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Given non-existent vehicle, when updating status, then should not persist changes")
    void shouldNotSaveWhenVehicleNotFound() {
        UUID id = UUID.randomUUID();

        when(gateway.findById(id)).thenReturn(Optional.empty());

        assertThrows(VehicleNotFoundException.class, () -> useCase.execute(id));
        verify(gateway, never()).save(any());
    }
}