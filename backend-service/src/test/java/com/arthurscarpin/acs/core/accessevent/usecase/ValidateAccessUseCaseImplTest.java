package com.arthurscarpin.acs.core.accessevent.usecase;

import com.arthurscarpin.acs.core.accessevent.domain.AccessEvent;
import com.arthurscarpin.acs.core.accessevent.domain.AccessResult;
import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import com.arthurscarpin.acs.core.accessevent.gateway.AccessEventGateway;
import com.arthurscarpin.acs.core.vehicle.domain.Plate;
import com.arthurscarpin.acs.core.vehicle.domain.Vehicle;
import com.arthurscarpin.acs.core.vehicle.domain.VehicleStatus;
import com.arthurscarpin.acs.core.vehicle.gateway.VehicleGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidateAccessUseCaseImplTest {

    @InjectMocks
    private ValidateAccessUseCaseImpl useCase;

    @Mock
    private VehicleGateway vehicleGateway;

    @Mock
    private AccessEventGateway accessEventGateway;

    @Test
    @DisplayName("Given active vehicle, when validating access, then should authorize access")
    void shouldAuthorizeAccessWhenVehicleIsActive() {
        AccessEvent input = AccessEvent.create("BRA1S23", OffsetDateTime.now(), Direction.IN, null);
        Plate plate = new Plate("BRA1S23");
        Vehicle vehicle = Vehicle.create(plate, "Audi A8", UUID.randomUUID());

        when(vehicleGateway.findByPlate("BRA1S23")).thenReturn(Optional.of(vehicle));
        when(accessEventGateway.save(any())).thenAnswer(inv -> inv.getArgument(0));
        AccessEvent response = useCase.execute(input);

        assertEquals(AccessResult.AUTHORIZED, response.result());
        verify(vehicleGateway).findByPlate("BRA1S23");
        verify(accessEventGateway).save(any(AccessEvent.class));
    }

    @Test
    @DisplayName("Given blocked vehicle, when validating access, then should deny access")
    void shouldDenyAccessWhenVehicleIsBlocked() {
        AccessEvent input = AccessEvent.create("BRA1S23", OffsetDateTime.now(), Direction.IN, null);
        Plate plate = new Plate("BRA1S23");
        Vehicle vehicle = Vehicle.create(plate, "Audi A8", UUID.randomUUID())
                .changeStatus(VehicleStatus.BLOCKED);

        when(vehicleGateway.findByPlate("BRA1S23")).thenReturn(Optional.of(vehicle));
        when(accessEventGateway.save(any())).thenAnswer(inv -> inv.getArgument(0));
        AccessEvent response = useCase.execute(input);

        assertEquals(AccessResult.DENIED, response.result());
    }

    @Test
    @DisplayName("Given vehicle not found, when validating access, then should deny access")
    void shouldDenyAccessWhenVehicleNotFound() {
        AccessEvent input = AccessEvent.create("BRA1S23", OffsetDateTime.now(), Direction.IN, null);

        when(vehicleGateway.findByPlate("BRA1S23")).thenReturn(Optional.empty());
        when(accessEventGateway.save(any())).thenAnswer(inv -> inv.getArgument(0));
        AccessEvent response = useCase.execute(input);

        assertEquals(AccessResult.DENIED, response.result());
        verify(vehicleGateway).findByPlate("BRA1S23");
        verify(accessEventGateway).save(any(AccessEvent.class));
    }

    @Test
    @DisplayName("Given valid access event, when validating access, then should persist event with correct data")
    void shouldPersistAccessEventWithCorrectData() {
        AccessEvent input = AccessEvent.create("BRA1S23", OffsetDateTime.now(), Direction.OUT, null);
        Plate plate = new Plate("BRA1S23");
        Vehicle vehicle = Vehicle.create(plate, "Audi A8", UUID.randomUUID());

        when(vehicleGateway.findByPlate("BRA1S23")).thenReturn(Optional.of(vehicle));
        when(accessEventGateway.save(any())).thenAnswer(inv -> inv.getArgument(0));
        useCase.execute(input);

        verify(accessEventGateway).save(
                argThat(event -> event.plate().equals("BRA1S23")
                                && event.direction().equals(Direction.OUT)
                                && event.result() == AccessResult.AUTHORIZED)
        );
    }

    @Test
    @DisplayName("Given vehicle gateway failure, when validating access, then should propagate exception")
    void shouldPropagateExceptionFromVehicleGateway() {
        AccessEvent input = AccessEvent.create("BRA1S23", OffsetDateTime.now(), Direction.IN, null);

        when(vehicleGateway.findByPlate(any())).thenThrow(new RuntimeException("Database error"));
        assertThrows(RuntimeException.class, () -> useCase.execute(input));

        verify(accessEventGateway, never()).save(any());
    }

    @Test
    @DisplayName("Given access event save failure, when validating access, then should propagate exception")
    void shouldPropagateExceptionOnSave() {
        AccessEvent input = AccessEvent.create("BRA1S23", OffsetDateTime.now(), Direction.OUT, null);
        Plate plate = new Plate("BRA1S23");
        Vehicle vehicle = Vehicle.create(plate, "Audi A8", UUID.randomUUID());

        when(vehicleGateway.findByPlate("BRA1S23")).thenReturn(Optional.of(vehicle));
        when(accessEventGateway.save(any())).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> useCase.execute(input));
    }
}