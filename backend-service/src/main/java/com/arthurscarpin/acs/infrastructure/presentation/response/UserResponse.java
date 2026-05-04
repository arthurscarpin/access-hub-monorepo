package com.arthurscarpin.acs.infrastructure.presentation.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

public record UserResponse(

        @Schema(description = "Unique identifier of the user", example = "00000000-0000-0000-0000-000000000001")
        UUID id,

        @Schema(description = "User full name", example = "Ana Santos")
        String name,

        @Schema(description = "User email address", example = "contact@example.com")
        String email,

        @Schema(description = "List of user scopes", example = """
            [
            "00000000-0000-0000-0000-000000000001",
            "00000000-0000-0000-0000-000000000002"
            ]
            """)
        List<String> scopes
) {
}