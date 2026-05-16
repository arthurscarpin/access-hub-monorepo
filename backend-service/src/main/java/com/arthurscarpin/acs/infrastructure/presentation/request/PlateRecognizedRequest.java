package com.arthurscarpin.acs.infrastructure.presentation.request;

import com.arthurscarpin.acs.core.capture.domain.Capture;

public record PlateRecognizedRequest(
        Capture capture,
        String error
) {
}
