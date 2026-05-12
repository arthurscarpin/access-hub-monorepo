package com.arthurscarpin.acs.core.capture.usecase;

import com.arthurscarpin.acs.core.capture.domain.Capture;
import com.arthurscarpin.acs.core.capture.domain.CaptureImage;
import com.arthurscarpin.acs.core.capture.domain.CaptureOCR;
import com.arthurscarpin.acs.core.capture.domain.CaptureOCRStatus;
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
                statusRequest.status(),
                ocr,
                Instant.now()
        );
    }

    @Override
    public void execute(CaptureOCRStatus captureOCRStatus) {
        Capture capture = gateway.findByCaptureIdAndImageId(captureOCRStatus.captureId(), captureOCRStatus.imageId());

        List<CaptureImage> updatedImages = capture.images().stream()
                .map(img -> img.id().equals(captureOCRStatus.imageId())
                        ? mapToUpdatedImage(img, captureOCRStatus)
                        : img)
                .toList();

        Capture updatedCapture = new Capture(
                capture.id(),
                updatedImages,
                capture.status(),
                capture.finalPlate(),
                capture.finalConfidence(),
                capture.createdAt(),
                Instant.now(),
                capture.processedAt(),
                capture.processedImagesCount(),
                capture.version()
        );

        Capture updatedResult = gateway.update(updatedCapture);
        System.out.println("salvou a parada");

        if (updatedResult.processedImagesCount() >= updatedResult.images().size()) {
            // TODO - Implements AI Producer
            System.out.println("To send to AI Producer");
        }
    }
}
