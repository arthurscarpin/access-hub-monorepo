package com.arthurscarpin.acs.core.capture.domain;

public record AIProgress(
        String finalPlate,
        Double confidence,
        String reasoning
) {
}
