package com.arthurscarpin.acs.infrastructure.presentation.documentation;

import com.arthurscarpin.acs.infrastructure.presentation.request.LoginRequest;
import com.arthurscarpin.acs.infrastructure.presentation.response.ErrorResponse;
import com.arthurscarpin.acs.infrastructure.presentation.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Authentication", description = "Operations related to user authentication")
public interface LoginControllerDoc {

    @Operation(summary = "User login", description = "Authenticates a user with email and password, returning a token and expiration time.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    LoginResponse login(@Valid @RequestBody LoginRequest request);
}
