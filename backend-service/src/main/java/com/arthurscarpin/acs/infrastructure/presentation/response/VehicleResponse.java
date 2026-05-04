package com.arthurscarpin.acs.infrastructure.presentation.response;

import com.arthurscarpin.acs.core.vehicle.domain.VehicleStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record VehicleResponse(

        @Schema(description = "Unique identifier of the vehicle", example = "00000000-0000-0000-0000-000000000001")
        UUID id,

        @Schema(description = "Vehicle plate", example = "BRA1S23")
        String plate,

        @Schema(description = "Vehicle model", example = "Audi A8")
        String model,

        @Schema(description = "Current vehicle status", example = "ACTIVE")
        VehicleStatus status,

        @Schema(description = "Identifier of the vehicle owner", example = "00000000-0000-0000-0000-000000000001")
        UUID ownerId
) {
}