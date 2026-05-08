package com.arthurscarpin.acs.infrastructure.presentation.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CaptureResponse(
        @Schema(description = "Capture ID", example = "00000000-0000-0000-0000-000000000001")
        String captureId,

        @Schema(description = "Message", example = "Capture registered successfully")
        String message
) {
}
