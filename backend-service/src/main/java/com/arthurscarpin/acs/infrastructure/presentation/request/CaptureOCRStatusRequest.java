package com.arthurscarpin.acs.infrastructure.presentation.request;

import com.arthurscarpin.acs.core.capture.domain.CaptureOCR;
import com.arthurscarpin.acs.core.capture.domain.ImageStatus;

import java.time.Instant;
import java.util.List;

public record CaptureOCRStatusRequest(
        String captureId,
        String imageId,
        String filename,
        Instant timestamp,
        ImageStatus status,
        String message,
        List<CaptureOCR> ocr
) {
}
