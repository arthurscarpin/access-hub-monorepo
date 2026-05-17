package com.arthurscarpin.acs.core.capture.usecase;

import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import com.arthurscarpin.acs.core.capture.domain.Capture;
import com.arthurscarpin.acs.core.capture.domain.CaptureImage;
import com.arthurscarpin.acs.core.capture.domain.CaptureStatus;
import com.arthurscarpin.acs.core.capture.domain.ImageStatus;
import com.arthurscarpin.acs.core.capture.exception.CaptureZipException;
import com.arthurscarpin.acs.core.capture.gateway.CaptureGateway;
import com.arthurscarpin.acs.core.capture.gateway.CaptureZipGateway;
import com.arthurscarpin.acs.infrastructure.util.UUIDExtractor;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

public class CreateCaptureUseCaseImpl implements CreateCaptureUseCase {

    private final CaptureGateway captureGateway;
    private final CaptureZipGateway captureZipGateway;

    public CreateCaptureUseCaseImpl(CaptureGateway captureGateway, CaptureZipGateway captureZipGateway) {
        this.captureGateway = captureGateway;
        this.captureZipGateway = captureZipGateway;
    }

    @Override
    public Capture execute(String filename, Direction direction) {
        String captureId = filename.replace(".zip", "");

        try {
            List<String> extractedFiles = captureZipGateway.zipProcessCapture(captureId);
            List<CaptureImage> captureImages = extractedFiles.stream()
                    .map(filePath -> {
                        String pureFileName = Path.of(filePath).getFileName().toString();
                        String imageUuid = UUIDExtractor.extract(pureFileName);
                        return new CaptureImage(
                                imageUuid,
                                filePath,
                                ImageStatus.RECEIVED,
                                List.of(),
                                Instant.now()
                        );
                    }).toList();
            Capture capture = new Capture(
                    captureId,
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

            return captureGateway.saveAndPublish(capture);

        } catch (CaptureZipException e) {
            return new Capture(
                    captureId,
                    List.of(),
                    CaptureStatus.FAILED,
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
        }
    }
}
