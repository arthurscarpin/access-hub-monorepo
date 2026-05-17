package com.arthurscarpin.acs.infrastructure.gateway;

import com.arthurscarpin.acs.core.capture.exception.CaptureNotFoundException;
import com.arthurscarpin.acs.core.capture.exception.CaptureZipException;
import com.arthurscarpin.acs.core.capture.gateway.CaptureZipGateway;
import com.arthurscarpin.acs.infrastructure.configuration.StorageConfigProperties;
import com.arthurscarpin.acs.infrastructure.zip.CaptureZipExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CaptureZipProcessGateway implements CaptureZipGateway {

    private final StorageConfigProperties properties;
    private final CaptureZipExtractor captureZipExtractor;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public List<String> zipProcessCapture(String captureId) {
        Path path = Path.of(properties.getRoot(), captureId + ".zip");

        try {
            if (!Files.exists(path)) {
                throw new CaptureNotFoundException("ZIP filename not found: " + path);
            }

            List<String> imagesFilenames = captureZipExtractor.validateContents(path);
            List<String> extractedPaths = captureZipExtractor.extract(path, captureId, imagesFilenames);
            captureZipExtractor.moveToBackup(path, captureId);
            return extractedPaths;

        } catch (CaptureNotFoundException | CaptureZipException e) {
            if (Files.exists(path)) {
                captureZipExtractor.moveToError(path, captureId);
            }
            throw e;
        } catch (Exception e) {
            if (Files.exists(path)) {
                captureZipExtractor.moveToError(path, captureId);
            }
            throw new CaptureZipException("ZIP Error: " + e.getMessage());
        }
    }
}
