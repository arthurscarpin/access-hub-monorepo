package com.arthurscarpin.acs.core.capture.domain;

public enum ImageStatus {
    RECEIVED,
    STARTED,
    PROCESSING,
    COMPLETED,
    FAILED;

    public static ImageStatus fromString(String value) {
        return valueOf(value.toUpperCase());
    }
}
