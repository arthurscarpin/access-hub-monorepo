package com.arthurscarpin.acs.infrastructure.presentation.request;

import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.OffsetDateTime;

public record AccessEventRequest(

        @NotBlank
        @Schema(description = "Vehicle plate", example = "BRA1S23")
        String plate,

        @NotNull
        @Schema(description = "Access direction", example = "IN")
        Direction direction,

        @NotNull
        @PastOrPresent
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX")
        @Schema(description = "Event timestamp", example = "2026-01-01 00:00:00-03:00")
        OffsetDateTime timestamp
) {
}