package com.arthurscarpin.acs.infrastructure.zip;

import com.arthurscarpin.acs.core.capture.exception.CaptureNotFoundException;
import com.arthurscarpin.acs.core.capture.exception.CaptureZipException;
import com.arthurscarpin.acs.infrastructure.configuration.StorageConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Component
@RequiredArgsConstructor
public class CaptureZipExtractor {

    private final StorageConfigProperties properties;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public List<String> validateContents(Path path) {
        Set<String> allowedExtensions = Set.of("png", "jpg", "jpeg");
        List<String> captureImagesFilenames;

        try (ZipFile zip = new ZipFile(path.toFile())) {
            captureImagesFilenames = zip.stream()
                    .filter(entry -> {
                        String name = entry.getName();
                        return !entry.isDirectory() &&
                                !name.startsWith("__MACOSX") &&
                                !name.startsWith(".") &&
                                !name.contains("/.");
                    })
                    .map(ZipEntry::getName)
                    .peek(name -> {
                        int dot = name.lastIndexOf('.');
                        String extension = (dot > 0) ? name.substring(dot + 1).toLowerCase() : "";
                        if (!allowedExtensions.contains(extension)) {
                            throw new CaptureZipException("Invalid file extension on zip: " + extension + ". Allowed: " + allowedExtensions);
                        }
                    })
                    .toList();
        } catch (IOException e) {
            throw new CaptureNotFoundException("Zip file not found: " + path);
        }

        if (captureImagesFilenames.isEmpty()) {
            throw new CaptureZipException("No capture images filenames found");
        }

        if (captureImagesFilenames.size() > properties.getMaxFiles()) {
            throw new CaptureZipException("ZIP contains " + captureImagesFilenames.size() +
                    " capture images, but the limit is " + properties.getMaxFiles());
        }

        return captureImagesFilenames;
    }

    public List<String> extract(Path zipPath, String uuid, List<String> imageNames) {
        Path tmp = Path.of(properties.getTmp(), uuid);

        try {
            Files.createDirectories(tmp);

            try (ZipFile zip = new ZipFile(zipPath.toFile())) {
                return imageNames.stream()
                        .map(zip::getEntry)
                        .filter(entry -> entry != null && !entry.isDirectory())
                        .map(entry -> {
                            String fileName = Path.of(entry.getName()).getFileName().toString();
                            Path destination = tmp.resolve(fileName);
                            try (InputStream in = zip.getInputStream(entry)) {
                                Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
                                return destination.toString();
                            } catch (IOException e) {
                                throw new CaptureNotFoundException("Zip processor error: " + entry.getName());
                            }
                        })
                        .toList();
            }
        } catch (IOException e) {
            throw new CaptureNotFoundException("Zip processor error: " + zipPath);
        }
    }

    public void moveToBackup(Path zipPath, String uuid) {
        String today = LocalDate.now().format(DATE_FORMATTER);
        Path backupDir = Path.of(properties.getBackup(), today);
        moveTo(zipPath, backupDir, uuid + ".zip");
        silentDelete(zipPath);
    }

    public void moveToError(Path zipPath, String uuid) {
        if (!Files.exists(zipPath)) {
            throw new CaptureNotFoundException("ZIP filename not found: " + zipPath);
        }
        String today = LocalDate.now().format(DATE_FORMATTER);
        Path errorDir = Path.of(properties.getError(), today);
        moveTo(zipPath, errorDir, uuid + ".zip");
    }

    private void moveTo(Path source, Path targetDir, String fileName) {
        try {
            Files.createDirectories(targetDir);
            Path destination = targetDir.resolve(fileName);
            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new CaptureNotFoundException("Can't move " + fileName + " to " + targetDir);
        }
    }

    private void silentDelete(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new CaptureNotFoundException("Can't delete " + path);
        }
    }
}
