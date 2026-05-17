package com.arthurscarpin.acs.infrastructure.presentation.request;

import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request used to create a capture from a ZIP file in storage.")
public record CaptureRequest(
        @NotBlank
        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\\.zip")
        @Schema(
                description = "ZIP filename available in the configured storage root. The capture id is derived from this UUID filename.",
                example = "550e8400-e29b-41d4-a716-446655440000.zip",
                pattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\\.zip"
        )
        String filename,

        @Schema(description = "Vehicle direction associated with the capture.", example = "IN")
        Direction direction
) {
}
