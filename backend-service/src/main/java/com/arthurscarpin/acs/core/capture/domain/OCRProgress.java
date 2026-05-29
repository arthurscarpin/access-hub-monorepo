package com.arthurscarpin.acs.core.capture.domain;

import java.util.List;

public record OCRProgress(
        String imageId,
        ImageStatus status,
        List<CaptureOCR> ocr
) {
}
