package com.arthurscarpin.acs.core.capture.usecase;

import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import com.arthurscarpin.acs.core.capture.domain.*;
import com.arthurscarpin.acs.core.capture.gateway.CaptureGateway;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class CreateCaptureUseCaseImpl implements CreateCaptureUseCase {

    private final CaptureGateway captureGateway;

    public CreateCaptureUseCaseImpl(CaptureGateway gateway) {
        this.captureGateway = gateway;
    }

    @Override
    public String execute(List<String> filenames, Direction direction) {
        List<CaptureImage> captureImages = filenames.stream()
                .map(filename -> new CaptureImage(
                        UUID.randomUUID().toString(),
                        filename,
                        ImageStatus.RECEIVED,
                        List.of(),
                        Instant.now()
                )).toList();

        Capture capture = new Capture(
                null,
                captureImages,
                CaptureStatus.RECEIVED,
                direction,
                null,
                null,
                null,
                Instant.now(),
                Instant.now(),
                null,
                0,
                null
        );

        return captureGateway.saveAndPublish(capture).id();
    }
}
