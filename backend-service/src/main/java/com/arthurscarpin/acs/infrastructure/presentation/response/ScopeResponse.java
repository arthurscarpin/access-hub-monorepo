package com.arthurscarpin.acs.infrastructure.presentation.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record ScopeResponse(

        @Schema(description = "Scope ID", example = "00000000-0000-0000-0000-000000000001")
        UUID id,

        @Schema(description = "Scope name", example = "admin:all")
        String name
) {
}
