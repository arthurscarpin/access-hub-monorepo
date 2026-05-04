package com.arthurscarpin.acs.infrastructure.presentation.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ErrorField(

        @Schema(description = "Field name", example = "field")
        String field,

        @Schema(description = "Error message", example = "Validation error message")
        String message
) {
}
