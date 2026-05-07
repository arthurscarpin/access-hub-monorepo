package com.arthurscarpin.acs.core.capture.domain;

import java.time.Instant;

public record CaptureImage(
        String id,

        String filename,

        String ocrText,

        Double confidence,

        ImageStatus status,

        Instant timestamp
) {
}
