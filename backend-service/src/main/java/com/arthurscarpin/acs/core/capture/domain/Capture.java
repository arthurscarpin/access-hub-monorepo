package com.arthurscarpin.acs.core.capture.domain;

import java.time.Instant;
import java.util.List;

public record Capture(
    String id,

    List<CaptureImage> images,

    CaptureStatus status,

    String finalPlate,

    Double finalConfidence,

    Instant createdAt,

    Instant updatedAt,

    Instant processedAt
) {
}
