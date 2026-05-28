package com.arthurscarpin.acs.core.vehicle.usecase;

import com.arthurscarpin.acs.core.pagination.PageInput;
import com.arthurscarpin.acs.core.pagination.PageOutput;
import com.arthurscarpin.acs.core.vehicle.domain.Plate;
import com.arthurscarpin.acs.core.vehicle.domain.Vehicle;
import com.arthurscarpin.acs.core.vehicle.gateway.VehicleGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetVehiclesUseCaseImplTest {

    @InjectMocks
    private GetVehiclesUseCaseImpl useCase;

    @Mock
    private VehicleGateway gateway;

    @Test
    @DisplayName("Given valid PageInput, when executing use case, then should return PageOutput from gateway")
    void shouldReturnPageOutputFromGateway() {
        PageInput input = new PageInput(
                0,
                10,
                "BRA1S23",
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-12-31T00:00:00Z")
        );

        Vehicle vehicle = Vehicle.create(
                new Plate("BRA1S23"),
                "Audi A8",
                UUID.randomUUID()
        );

        PageOutput<Vehicle> expected = new PageOutput<>(
                List.of(vehicle),
                0,
                10,
                1L,
                1
        );

        when(gateway.findByFilters(input)).thenReturn(expected);

        PageOutput<Vehicle> result = useCase.execute(input);

        assertNotNull(result);
        assertEquals(expected, result);

        verify(gateway, times(1)).findByFilters(input);
    }

    @Test
    @DisplayName("Given PageInput with filters, when executing, then should pass same filters to gateway")
    void shouldPassFiltersCorrectlyToGateway() {
        PageInput input = new PageInput(
                2,
                5,
                "BRA1S23",
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-06-01T00:00:00Z")
        );

        when(gateway.findByFilters(any())).thenReturn(
                new PageOutput<>(List.of(), 2, 5, 0, 0)
        );

        useCase.execute(input);

        ArgumentCaptor<PageInput> captor = ArgumentCaptor.forClass(PageInput.class);
        verify(gateway).findByFilters(captor.capture());

        PageInput captured = captor.getValue();

        assertEquals(2, captured.pageNumber());
        assertEquals(5, captured.pageSize());
        assertEquals("BRA1S23", captured.plate());
        assertEquals(input.from(), captured.from());
        assertEquals(input.to(), captured.to());
    }

    @Test
    @DisplayName("Given gateway response, when executing, then should return same instance without modification")
    void shouldReturnSameInstance() {
        PageInput input = new PageInput(
                0,
                10,
                null,
                null,
                null
        );

        PageOutput<Vehicle> output = new PageOutput<>(
                List.of(),
                0,
                10,
                0,
                0
        );

        when(gateway.findByFilters(input)).thenReturn(output);

        PageOutput<Vehicle> result = useCase.execute(input);

        assertSame(output, result);
        verify(gateway).findByFilters(input);
        verifyNoMoreInteractions(gateway);
    }
}