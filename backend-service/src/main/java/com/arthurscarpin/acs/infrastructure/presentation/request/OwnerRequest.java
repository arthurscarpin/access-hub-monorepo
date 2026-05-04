package com.arthurscarpin.acs.infrastructure.presentation.request;

import com.arthurscarpin.acs.core.owner.domain.DocumentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OwnerRequest(

        @NotBlank
        @Size(min = 3, max = 100)
        @Schema(description = "Owner full name", example = "Maria Oliveira")
        String name,

        @NotBlank
        @Size(min = 9, max = 20)
        @Schema(description = "Document number", example = "111.444.777-35")
        String document,

        @NotNull
        @Schema(description = "Type of document", example = "CPF")
        DocumentType documentType,

        @NotBlank
        @Email
        @Schema(description = "Owner email address", example = "contact@example.com")
        String email
) {
}