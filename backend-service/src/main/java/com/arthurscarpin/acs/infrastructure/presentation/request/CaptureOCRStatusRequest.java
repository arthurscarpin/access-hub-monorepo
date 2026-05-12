package com.arthurscarpin.acs.infrastructure.presentation.request;

import com.arthurscarpin.acs.core.capture.domain.CaptureOCR;
import com.arthurscarpin.acs.core.capture.domain.CaptureStatus;
import com.arthurscarpin.acs.core.capture.domain.ImageStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public record CaptureOCRStatusRequest(
        String captureId,
        String imageId,
        String filename,
        Instant timestamp,

        @JsonProperty("image_status")
        ImageStatus imageStatus,
        
        @JsonProperty("capture_status")
        CaptureStatus captureStatus,
        
        String message,
        List<CaptureOCR> ocr
) {
}
