package com.arthurscarpin.acs.infrastructure.presentation.documentation;

import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import com.arthurscarpin.acs.infrastructure.presentation.response.CaptureResponse;
import com.arthurscarpin.acs.infrastructure.presentation.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Captures", description = "Operations related to captures")
public interface CaptureControllerDoc {

    @Operation(
            summary = "Upload capture ZIP",
            description = "Creates a capture from an uploaded ZIP file. The ZIP filename must be a UUID with the .zip extension. The service stores the upload, validates and extracts the images, stores the capture document, and publishes one OCR job per extracted image."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Capture created successfully from the ZIP file", content = @Content(schema = @Schema(implementation = CaptureResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filename or request data", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    CaptureResponse save(
            @RequestPart("file")
            MultipartFile file,

            @RequestParam("direction")
            Direction direction
    );
}
