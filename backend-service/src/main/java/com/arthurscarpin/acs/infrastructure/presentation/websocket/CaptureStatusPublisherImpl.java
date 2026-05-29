package com.arthurscarpin.acs.infrastructure.presentation.websocket;

import com.arthurscarpin.acs.core.capture.domain.CaptureRealtimeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CaptureStatusPublisherImpl implements CaptureStatusPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void publish(CaptureRealtimeEvent<?> event) {
        String destination = "/topic/capture/" + event.captureId();
        log.info("Websocket destination: {}", destination);
        messagingTemplate.convertAndSend(destination, event);
        log.info("Websocket - message sent: {}", event);
    }
}