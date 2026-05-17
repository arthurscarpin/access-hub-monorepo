package com.arthurscarpin.acs.infrastructure.presentation.request;

import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CaptureRequest(
        @NotBlank
        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\\.zip")
        String filename,
        Direction direction
) {
}
