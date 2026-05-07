package com.arthurscarpin.acs.core.capture.domain;

import java.time.Instant;

public record CaptureMessage(
        String captureId,
        String imageId,
        String filename,
        Instant timestamp
) {
}
