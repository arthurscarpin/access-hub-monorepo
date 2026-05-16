package com.arthurscarpin.acs.core.capture.usecase;

import com.arthurscarpin.acs.core.accessevent.domain.AccessEvent;
import com.arthurscarpin.acs.core.accessevent.usecase.ValidateAccessUseCase;
import com.arthurscarpin.acs.core.capture.domain.Capture;
import com.arthurscarpin.acs.core.capture.gateway.CaptureGateway;

import java.time.Instant;
import java.time.OffsetDateTime;

public class ResultCaptureUseCaseImpl implements ResultCaptureUseCase {

    private final CaptureGateway gateway;

    private final ValidateAccessUseCase useCase;

    public ResultCaptureUseCaseImpl(CaptureGateway gateway, ValidateAccessUseCase useCase) {
        this.gateway = gateway;
        this.useCase = useCase;
    }

    @Override
    public void execute(Capture domain) {
        Capture updatedCapture = new Capture(
                domain.id(),
                domain.images(),
                domain.status(),
                domain.direction(),
                domain.finalPlate(),
                domain.finalConfidence(),
                domain.reasoning(),
                domain.createdAt(),
                domain.updatedAt(),
                Instant.now(),
                domain.processedImagesCount(),
                domain.version()
        );

        gateway.update(updatedCapture);

        AccessEvent accessEvent = new AccessEvent(
                null,
                domain.finalPlate(),
                OffsetDateTime.now(),
                domain.direction(),
                null
        );
        useCase.execute(accessEvent);
    }
}
