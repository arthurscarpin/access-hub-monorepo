package com.arthurscarpin.acs.core.capture.domain;

public record CaptureRealtimeEvent<T>(
        String captureId,
        CaptureEventType type,
        T payload,
        long timestamp
) {}
