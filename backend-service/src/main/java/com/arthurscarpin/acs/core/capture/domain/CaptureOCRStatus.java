package com.arthurscarpin.acs.core.capture.domain;

import java.time.Instant;
import java.util.List;

public record CaptureOCRStatus(
        String captureId,
        String imageId,
        String filename,
        Instant timestamp,
        ImageStatus imageStatus,
        CaptureStatus captureStatus,
        String message,
        List<CaptureOCR> ocr
) {
}
