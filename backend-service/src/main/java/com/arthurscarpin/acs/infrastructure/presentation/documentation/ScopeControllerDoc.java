package com.arthurscarpin.acs.infrastructure.presentation.documentation;

import com.arthurscarpin.acs.infrastructure.presentation.response.ScopeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Scopes", description = "Operations related to scopes")
public interface ScopeControllerDoc {

    @Operation(summary = "Find all scopes", description = "Retrieves a list of all available scopes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Scopes retrieved successfully", content = @Content(schema = @Schema(implementation = ScopeResponse.class)))
    })
    List<ScopeResponse> findAll();
}

