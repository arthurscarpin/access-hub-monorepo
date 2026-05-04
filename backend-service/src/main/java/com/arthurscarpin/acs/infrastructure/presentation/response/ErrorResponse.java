package com.arthurscarpin.acs.infrastructure.presentation.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record ErrorResponse(

        @Schema(description = "Timestamp when the error occurred", example = "2026-01-01T00:00:00Z")
        OffsetDateTime timestamp,

        @Schema(description = "HTTP status code", example = "400")
        Integer status,

        @Schema(description = "General error message", example = "Error message")
        String message,

        @Schema(description = "List of field validation errors")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<ErrorField> errors
) {
}
