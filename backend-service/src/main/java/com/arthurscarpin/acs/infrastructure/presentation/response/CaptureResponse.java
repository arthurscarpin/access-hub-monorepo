package com.arthurscarpin.acs.infrastructure.presentation.response;

import com.arthurscarpin.acs.core.capture.domain.CaptureStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Response returned after a capture ZIP is processed.")
public record CaptureResponse(
        @Schema(description = "Capture identifier derived from the ZIP filename.", example = "550e8400-e29b-41d4-a716-446655440000")
        String id,

        @Schema(description = "Initial capture status after ZIP processing.", example = "RECEIVED")
        CaptureStatus status,

        @Schema(description = "Human-readable result message.", example = "Capture created with success")
        String message,

        @Schema(description = "Image file paths extracted from the ZIP and registered for OCR processing.")
        List<String> extractedFiles
) {
}
