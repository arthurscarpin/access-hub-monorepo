package com.arthurscarpin.acs.core.capture.domain;

import java.time.Instant;
import java.util.List;

public record CaptureImage(
        String id,
        String filename,
        ImageStatus status,
        List<CaptureOCR> ocr,
        Instant timestamp
) {
}
