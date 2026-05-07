package com.arthurscarpin.acs.infrastructure.presentation.controller;

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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CaptureResponse save(@Valid @RequestBody CaptureRequest request) {
        log.info("Starting capture creation with {} filenames", request.filenames().size());
        List<String> filenames = request.filenames();
        log.debug("Processing filenames: {}", filenames);
        String captureId = createCaptureUseCase.execute(filenames);
        log.debug("Generated capture ID: {}", captureId);
        log.info("Capture creation completed successfully with ID: {}", captureId);
        return new CaptureResponse(captureId, "Capture registered successfully");
    }
}
