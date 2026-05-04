package com.arthurscarpin.acs.infrastructure.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.UUID;

public record UserRequest(

        @NotBlank
        @Schema(description = "Username", example = "Ana Santos")
        String name,

        @NotBlank
        @Email
        @Schema(description = "User email address", example = "contact@example.com")
        String email,

        @NotBlank
        @Size(min = 6, max = 20)
        @Schema(description = "User password", example = "Password@123")
        String password,

        @NotNull
        @NotEmpty
        @Schema(description = "List of scope IDs assigned to the user", example = """
            [
            "00000000-0000-0000-0000-000000000001",
            "00000000-0000-0000-0000-000000000002"
            ]
            """
        )
        List<UUID> scopes
) {
}