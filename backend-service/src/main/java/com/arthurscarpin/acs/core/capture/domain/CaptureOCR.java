package com.arthurscarpin.acs.core.capture.domain;

import java.util.List;

public record CaptureOCR(
        String text,
        Double confidence,
        List<List<Integer>> bbox
) {
}
