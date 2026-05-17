package com.arthurscarpin.acs.infrastructure.presentation.controller;

import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import com.arthurscarpin.acs.core.capture.domain.Capture;
import com.arthurscarpin.acs.core.capture.domain.CaptureImage;
import com.arthurscarpin.acs.core.capture.usecase.CreateCaptureUseCase;
import com.arthurscarpin.acs.infrastructure.configuration.annotations.CanWriteCapture;
import com.arthurscarpin.acs.infrastructure.presentation.documentation.CaptureControllerDoc;
import com.arthurscarpin.acs.infrastructure.presentation.request.CaptureRequest;
import com.arthurscarpin.acs.infrastructure.presentation.response.CaptureResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/captures")
@RequiredArgsConstructor
public class CaptureController implements CaptureControllerDoc {

    private final CreateCaptureUseCase createCaptureUseCase;

    @CanWriteCapture
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public CaptureResponse save(@Valid @RequestBody CaptureRequest request) {
        String filename = request.filename();
        Direction direction = request.direction();
        log.info("Starting capture creation with {} filename", request.filename());
        log.debug("Processing filename: {}", filename);
        Capture capture = createCaptureUseCase.execute(filename, direction);
        log.debug("Capture created: {}", capture);
        List<String> captureImages = capture.images().stream()
                .map(CaptureImage::filename)
                .toList();
        return new CaptureResponse(
                capture.id(),
                capture.status(),
                "Capture created with success",
                captureImages
        );
    }
}
