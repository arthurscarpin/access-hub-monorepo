package com.arthurscarpin.acs.infrastructure.presentation.response;

import com.arthurscarpin.acs.core.accessevent.domain.AccessResult;
import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AccessEventResponse(

        @Schema(description = "Unique identifier of the access event", example = "00000000-0000-0000-0000-000000000001")
        UUID id,

        @Schema(description = "Vehicle plate", example = "BRA1S23")
        String plate,

        @Schema(description = "Timestamp of the access event", example = "2026-01-01T00:00:00Z")
        OffsetDateTime timestamp,

        @Schema(description = "Direction of the vehicle", example = "IN")
        Direction direction,

        @Schema(description = "Result of the access validation", example = "AUTHORIZED")
        AccessResult result
) {
}