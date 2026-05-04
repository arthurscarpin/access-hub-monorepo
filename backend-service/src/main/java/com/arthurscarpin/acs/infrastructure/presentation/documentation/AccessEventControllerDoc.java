package com.arthurscarpin.acs.infrastructure.presentation.documentation;

import com.arthurscarpin.acs.infrastructure.presentation.request.AccessEventRequest;
import com.arthurscarpin.acs.infrastructure.presentation.response.AccessEventResponse;
import com.arthurscarpin.acs.infrastructure.presentation.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Access Events", description = "Operations related to access events")
public interface AccessEventControllerDoc {

    @Operation(summary = "Validate and record access event", description = "Validates access for a vehicle based on plate, direction, and timestamp, and records the event.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Access event created successfully", content = @Content(schema = @Schema(implementation = AccessEventResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    AccessEventResponse save(@Valid @RequestBody AccessEventRequest request);

    @Operation(summary = "Get access event history", description = "Retrieves a paginated list of access events, optionally filtered by plate, from date, and to date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access events retrieved successfully", content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Page<AccessEventResponse> findAll(
            @RequestParam(required = false) String plate,
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to,
            Pageable pageable
    );
}