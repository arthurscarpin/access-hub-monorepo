package com.arthurscarpin.acs.infrastructure.presentation.controller;

import com.arthurscarpin.acs.core.vehicle.domain.Vehicle;
import com.arthurscarpin.acs.core.vehicle.usecase.RegisterVehicleUseCase;
import com.arthurscarpin.acs.core.vehicle.usecase.UpdateVehicleStatusUseCase;
import com.arthurscarpin.acs.infrastructure.configuration.annotations.CanWriteVehicle;
import com.arthurscarpin.acs.infrastructure.mapper.VehicleMapper;
import com.arthurscarpin.acs.infrastructure.presentation.documentation.VehicleControllerDoc;
import com.arthurscarpin.acs.infrastructure.presentation.request.VehicleRequest;
import com.arthurscarpin.acs.infrastructure.presentation.response.VehicleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController implements VehicleControllerDoc {

    private final RegisterVehicleUseCase registerVehicleUseCase;

    private final UpdateVehicleStatusUseCase updateVehicleStatusUseCase;

    private final VehicleMapper mapper;

    @CanWriteVehicle
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponse save(@Valid @RequestBody VehicleRequest request) {
        log.info("Starting vehicle registration for plate: {}", request.plate());
        Vehicle domain = mapper.fromRequestToDomain(request);
        log.debug("Mapped request to domain: {}", domain);
        Vehicle response = registerVehicleUseCase.execute(domain);
        log.debug("Vehicle after registration: {}", response);
        log.info("Vehicle registration completed for plate: {}", response.plate());
        return mapper.fromDomainToResponse(response);
    }

    @CanWriteVehicle
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VehicleResponse findById(@PathVariable UUID id) {
        log.info("Updating vehicle status for id: {}", id);
        Vehicle response = updateVehicleStatusUseCase.execute(id);
        log.debug("Vehicle after status update: {}", response);
        log.info("Vehicle status updated for id: {}", id);
        return mapper.fromDomainToResponse(response);
    }
}
