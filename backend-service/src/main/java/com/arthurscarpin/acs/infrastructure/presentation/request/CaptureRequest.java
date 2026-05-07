package com.arthurscarpin.acs.infrastructure.presentation.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CaptureRequest(

        @NotNull
        @NotEmpty
        List<String> filenames
) {
}
