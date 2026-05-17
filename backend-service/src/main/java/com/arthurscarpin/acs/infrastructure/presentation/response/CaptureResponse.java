package com.arthurscarpin.acs.infrastructure.presentation.response;

import com.arthurscarpin.acs.core.capture.domain.CaptureStatus;

import java.util.List;

public record CaptureResponse(
        String id,
        CaptureStatus status,
        String message,
        List<String> extractedFiles
) {
}
