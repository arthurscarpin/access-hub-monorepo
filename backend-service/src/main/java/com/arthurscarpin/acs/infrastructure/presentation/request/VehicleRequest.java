package com.arthurscarpin.acs.infrastructure.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record VehicleRequest(

        @NotBlank
        @Size(min = 5, max = 10)
        @Pattern(regexp = "^[a-zA-Z0-9- ]+$", message = "Plate must contain only letters and numbers")
        @Schema(description = "Vehicle plate", example = "BRA1S23")
        String plate,

        @NotBlank
        @Schema(description = "Vehicle model", example = "Audi A8")
        String model,

        @NotNull
        @Schema(description = "Owner ID reference", example = "00000000-0000-0000-0000-000000000001")
        UUID ownerId
) {
}