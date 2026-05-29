package com.arthurscarpin.acs.infrastructure.presentation.websocket;

import com.arthurscarpin.acs.core.capture.domain.CaptureRealtimeEvent;

public interface CaptureStatusPublisher {

    void publish(CaptureRealtimeEvent<?> event);
}