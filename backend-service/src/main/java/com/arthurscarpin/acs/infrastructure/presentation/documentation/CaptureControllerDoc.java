package com.arthurscarpin.acs.infrastructure.presentation.documentation;

import com.arthurscarpin.acs.infrastructure.presentation.request.CaptureRequest;
import com.arthurscarpin.acs.infrastructure.presentation.response.CaptureResponse;
import com.arthurscarpin.acs.infrastructure.presentation.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Captures", description = "Operations related to captures")
public interface CaptureControllerDoc {

    @Operation(
            summary = "Upload capture ZIP",
            description = "Creates a capture from a ZIP file already available in storage. The ZIP filename must be a UUID with the .zip extension. The service validates and extracts the images, stores the capture document, and publishes one OCR job per extracted image."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Capture created successfully from the ZIP file", content = @Content(schema = @Schema(implementation = CaptureResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filename or request data", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CaptureResponse save(
            @Valid
            @RequestBody
            CaptureRequest request
    );
}
