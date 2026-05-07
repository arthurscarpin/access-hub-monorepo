package com.arthurscarpin.acs.core.capture.usecase;

import com.arthurscarpin.acs.core.capture.domain.Capture;
import com.arthurscarpin.acs.core.capture.domain.CaptureImage;
import com.arthurscarpin.acs.core.capture.domain.CaptureStatus;
import com.arthurscarpin.acs.core.capture.domain.ImageStatus;
import com.arthurscarpin.acs.core.capture.gateway.CaptureGateway;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

public class CreateCaptureUseCaseImpl implements CreateCaptureUseCase {

    private final CaptureGateway gateway;

    public CreateCaptureUseCaseImpl(CaptureGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public String execute(List<String> filenames) {
        List<CaptureImage> captureImages = filenames.stream()
                .map(filename -> new CaptureImage(
                        null,
                        filename,
                        null,
                        null,
                        ImageStatus.RECEIVED,
                        null
                )).toList();

        Capture capture = new Capture(
                null,
                captureImages,
                CaptureStatus.RECEIVED,
                null,
                null,
                Instant.now(),
                Instant.now(),
                null
        );

        // TODO - To implement Capture producer with RabbitMQ
        return gateway.save(capture).id();
    }
}
