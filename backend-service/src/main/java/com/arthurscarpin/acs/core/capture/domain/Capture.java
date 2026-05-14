package com.arthurscarpin.acs.core.capture.domain;

import com.arthurscarpin.acs.core.accessevent.domain.Direction;

import java.time.Instant;
import java.util.List;

public record Capture(
    String id,
    List<CaptureImage> images,
    CaptureStatus status,
    Direction direction,
    String finalPlate,
    Double finalConfidence,
    String reasoning,
    Instant createdAt,
    Instant updatedAt,
    Instant processedAt,
    Integer processedImagesCount,
    Long version
) {
}
