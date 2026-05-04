package com.arthurscarpin.acs.infrastructure.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank
        @Email
        @Schema(description = "User email address", example = "contact@example.com")
        String email,

        @NotBlank
        @Schema(description = "User password", example = "Password@1234")
        String password
) {
}