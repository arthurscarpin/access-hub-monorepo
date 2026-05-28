package com.arthurscarpin.acs.infrastructure.presentation.controller;

import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import com.arthurscarpin.acs.core.capture.domain.Capture;
import com.arthurscarpin.acs.core.capture.domain.CaptureImage;
import com.arthurscarpin.acs.core.capture.exception.CaptureZipException;
import com.arthurscarpin.acs.core.capture.usecase.CreateCaptureUseCase;
import com.arthurscarpin.acs.infrastructure.configuration.StorageConfigProperties;
import com.arthurscarpin.acs.infrastructure.configuration.annotations.CanWriteCapture;
import com.arthurscarpin.acs.infrastructure.presentation.documentation.CaptureControllerDoc;
import com.arthurscarpin.acs.infrastructure.presentation.response.CaptureResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/captures")
@RequiredArgsConstructor
public class CaptureController implements CaptureControllerDoc {

    private static final Pattern ZIP_FILENAME_PATTERN = Pattern.compile("^[0-9a-fA-F\\-]{36}\\.zip$");

    private final CreateCaptureUseCase createCaptureUseCase;
    private final StorageConfigProperties storageConfigProperties;

    @CanWriteCapture
    @PostMapping(
            value = "/upload",
            consumes = "multipart/form-data"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public CaptureResponse save(
            @RequestPart("file") MultipartFile file,
            @RequestParam("direction") Direction direction
    ) {

        String filename = file.getOriginalFilename();

        if (filename == null || !ZIP_FILENAME_PATTERN.matcher(filename).matches()) {
            throw new CaptureZipException("Invalid ZIP file");
        }

        Path storagePath = Path.of(storageConfigProperties.getRoot());
        Path destination = storagePath.resolve(filename);

        log.info("Current working directory: {}", System.getProperty("user.dir"));
        log.info("Configured storage root: {}", storageConfigProperties.getRoot());
        log.debug("Absolute storage path: {}", storagePath.toAbsolutePath());
        log.debug("Destination path: {}", destination.toAbsolutePath());

        try {
            Files.createDirectories(storagePath);
            Files.copy(
                    file.getInputStream(),
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException e) {
            log.error("Failed to save ZIP file", e);
            throw new CaptureZipException("Could not save ZIP file: " + e.getMessage());
        }

        log.info("ZIP saved at {}", destination.toAbsolutePath());
        Capture capture = createCaptureUseCase.execute(filename, direction);
        List<String> captureImages = capture.images()
                .stream()
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