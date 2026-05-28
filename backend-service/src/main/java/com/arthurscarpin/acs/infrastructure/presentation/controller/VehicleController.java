package com.arthurscarpin.acs.infrastructure.presentation.controller;

import com.arthurscarpin.acs.core.pagination.PageInput;
import com.arthurscarpin.acs.core.pagination.PageOutput;
import com.arthurscarpin.acs.core.vehicle.domain.Vehicle;
import com.arthurscarpin.acs.core.vehicle.usecase.GetVehiclesUseCase;
import com.arthurscarpin.acs.core.vehicle.usecase.RegisterVehicleUseCase;
import com.arthurscarpin.acs.core.vehicle.usecase.UpdateVehicleStatusUseCase;
import com.arthurscarpin.acs.infrastructure.configuration.annotations.CanReadVehicle;
import com.arthurscarpin.acs.infrastructure.configuration.annotations.CanWriteVehicle;
import com.arthurscarpin.acs.infrastructure.mapper.VehicleMapper;
import com.arthurscarpin.acs.infrastructure.presentation.documentation.VehicleControllerDoc;
import com.arthurscarpin.acs.infrastructure.presentation.request.VehicleRequest;
import com.arthurscarpin.acs.infrastructure.presentation.response.VehicleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController implements VehicleControllerDoc {

    private final RegisterVehicleUseCase registerVehicleUseCase;

    private final UpdateVehicleStatusUseCase updateVehicleStatusUseCase;

    private final GetVehiclesUseCase getVehiclesUseCase;

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
    public VehicleResponse updateById(@PathVariable UUID id) {
        log.info("Updating vehicle status for id: {}", id);
        Vehicle response = updateVehicleStatusUseCase.execute(id);
        log.debug("Vehicle after status update: {}", response);
        log.info("Vehicle status updated for id: {}", id);
        return mapper.fromDomainToResponse(response);
    }

    @CanReadVehicle
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<VehicleResponse> findAll(Pageable pageable) {
        log.info("Starting vehicle search for pages: {}", pageable);
        PageInput pageInput = new PageInput(pageable.getPageNumber(), pageable.getPageSize(), null, null, null);
        log.debug("Page input created: {}", pageInput);
        PageOutput<Vehicle> response = getVehiclesUseCase.execute(pageInput);
        log.debug("Vehicle search completed for pages: {}", response);
        List<VehicleResponse> content = response.content()
                .stream()
                .map(mapper::fromDomainToResponse)
                .toList();
        log.debug("Mapped {} vehicles to response", content.size());
        log.info("User listing completed successfully - returned {} records", content.size());
        return new PageImpl<>(content, pageable, response.totalElements());
    }
}
