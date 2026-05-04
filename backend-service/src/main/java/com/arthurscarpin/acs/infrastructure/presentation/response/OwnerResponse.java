package com.arthurscarpin.acs.infrastructure.presentation.response;

import com.arthurscarpin.acs.core.owner.domain.DocumentType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record OwnerResponse(

        @Schema(description = "Unique identifier of the owner", example = "00000000-0000-0000-0000-000000000001")
        UUID id,

        @Schema(description = "Owner full name", example = "Maria Oliveira")
        String name,

        @Schema(description = "Owner document number", example = "111.444.777-35")
        String document,

        @Schema(description = "Type of document", example = "CPF")
        DocumentType documentType,

        @Schema(description = "Owner email address", example = "contact@example.com")
        String email
) {
}