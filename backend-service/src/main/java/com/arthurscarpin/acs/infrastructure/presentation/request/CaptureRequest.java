package com.arthurscarpin.acs.infrastructure.presentation.request;

import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CaptureRequest(

        @NotNull
        @NotEmpty
        @Schema(description = "Filename List", example = """
                [
                "filename1.jpg",
                "filename2.jpg",
                "filename3.jpg"
                ]
                """)
        List<String> filenames,

        @NotNull
        Direction direction
) {
}
