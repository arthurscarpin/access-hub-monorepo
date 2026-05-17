package com.arthurscarpin.acs.core.capture.usecase;

import com.arthurscarpin.acs.core.capture.domain.*;
import com.arthurscarpin.acs.core.capture.gateway.CaptureGateway;

import java.time.Instant;
import java.util.List;

public class StatusCaptureUseCaseImpl implements StatusCaptureUseCase {

    private final CaptureGateway gateway;

    public StatusCaptureUseCaseImpl(CaptureGateway gateway) {
        this.gateway = gateway;
    }

    private CaptureImage mapToUpdatedImage(CaptureImage currentImage, CaptureOCRStatus statusRequest) {
        List<CaptureOCR> ocr = statusRequest.ocr() == null ? List.of() : statusRequest.ocr().stream()
                .map(req -> new CaptureOCR(req.text(), req.confidence(), req.bbox()))
                .toList();
        return new CaptureImage(
                currentImage.id(),
                currentImage.filename(),
                statusRequest.imageStatus(),
                ocr,
                Instant.now()
        );
    }

    @Override
    public void execute(CaptureOCRStatus captureOCRStatus) {

        if (captureOCRStatus.imageStatus() != ImageStatus.COMPLETED) {
            return;
        }

        Capture capture = gateway.findById(captureOCRStatus.captureId());

        List<CaptureImage> updatedImages = capture.images().stream()
                .map(img -> img.id().equals(captureOCRStatus.imageId())
                        ? mapToUpdatedImage(img, captureOCRStatus)
                        : img)
                .toList();

        int totalProcessed = (int) updatedImages.stream()
                .filter(img -> img.status() == ImageStatus.COMPLETED)
                .count();

        boolean isBatchFinished = totalProcessed >= updatedImages.size();

        Capture updatedCapture = new Capture(
                capture.id(),
                updatedImages,
                isBatchFinished ? captureOCRStatus.captureStatus() : capture.status(),
                capture.direction(),
                capture.finalPlate(),
                capture.finalConfidence(),
                capture.reasoning(),
                capture.createdAt(),
                Instant.now(),
                capture.processedAt(),
                totalProcessed,
                capture.version()
        );

        gateway.updateAndPublish(updatedCapture, captureOCRStatus.imageId());
    }
}