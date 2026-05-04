package com.arthurscarpin.acs.infrastructure.presentation.documentation;

import com.arthurscarpin.acs.infrastructure.presentation.request.UserRequest;
import com.arthurscarpin.acs.infrastructure.presentation.response.ErrorResponse;
import com.arthurscarpin.acs.infrastructure.presentation.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Users", description = "Operations related to users")
public interface UserControllerDoc {

    @Operation(summary = "Register user", description = "Registers a new user with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully", content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    UserResponse save(@Valid @RequestBody UserRequest request);
}
