package com.arthurscarpin.acs.infrastructure.zip;

import com.arthurscarpin.acs.core.capture.exception.CaptureNotFoundException;
import com.arthurscarpin.acs.core.capture.exception.CaptureZipException;
import com.arthurscarpin.acs.infrastructure.configuration.StorageConfigProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class CaptureZipExtractorTest {

    @TempDir
    private Path tempDir;

    private StorageConfigProperties properties;
    private CaptureZipExtractor extractor;

    @BeforeEach
    void setup() {
        properties = new StorageConfigProperties();
        properties.setTmp(tempDir.resolve("tmp").toString());
        properties.setBackup(tempDir.resolve("backup").toString());
        properties.setError(tempDir.resolve("error").toString());
        properties.setMaxFiles(3);

        extractor = new CaptureZipExtractor(properties);
    }

    @Test
    @DisplayName("Given zip with valid images When validating contents Then should return image filenames")
    void shouldValidateZipWithValidImages() throws IOException {
        Path zipPath = createZip(
                "captures/front.jpg",
                "captures/back.png",
                "__MACOSX/ignored.jpg",
                ".hidden.jpg",
                "captures/.hidden.png"
        );

        List<String> result = extractor.validateContents(zipPath);

        assertEquals(List.of("captures/front.jpg", "captures/back.png"), result);
    }

    @Test
    @DisplayName("Given zip with invalid extension When validating contents Then should throw CaptureZipException")
    void shouldThrowWhenZipContainsInvalidExtension() throws IOException {
        Path zipPath = createZip("capture.jpg", "notes.txt");

        CaptureZipException exception = assertThrows(
                CaptureZipException.class,
                () -> extractor.validateContents(zipPath)
        );

        assertTrue(exception.getMessage().contains("Invalid file extension on zip"));
    }

    @Test
    @DisplayName("Given zip without capture images When validating contents Then should throw CaptureZipException")
    void shouldThrowWhenZipHasNoCaptureImages() throws IOException {
        Path zipPath = createZip("__MACOSX/ignored.jpg", ".hidden.png");

        CaptureZipException exception = assertThrows(
                CaptureZipException.class,
                () -> extractor.validateContents(zipPath)
        );

        assertEquals("No capture images filenames found", exception.getMessage());
    }

    @Test
    @DisplayName("Given zip above max files When validating contents Then should throw CaptureZipException")
    void shouldThrowWhenZipExceedsMaxFiles() throws IOException {
        Path zipPath = createZip("one.jpg", "two.jpg", "three.jpg", "four.jpg");

        CaptureZipException exception = assertThrows(
                CaptureZipException.class,
                () -> extractor.validateContents(zipPath)
        );

        assertTrue(exception.getMessage().contains("ZIP contains 4 capture images"));
    }

    @Test
    @DisplayName("Given missing zip file When validating contents Then should throw CaptureNotFoundException")
    void shouldThrowWhenZipDoesNotExist() {
        Path zipPath = tempDir.resolve("missing.zip");

        CaptureNotFoundException exception = assertThrows(
                CaptureNotFoundException.class,
                () -> extractor.validateContents(zipPath)
        );

        assertTrue(exception.getMessage().contains("Zip file not found"));
    }

    @Test
    @DisplayName("Given image names When extracting zip Then should copy files to capture tmp folder")
    void shouldExtractImagesToTmpFolder() throws IOException {
        String captureId = "capture-123";
        Path zipPath = createZip("nested/front.jpg", "back.png", "ignored.jpeg");

        List<String> result = extractor.extract(
                zipPath,
                captureId,
                List.of("nested/front.jpg", "back.png", "missing.jpg")
        );

        Path expectedFront = Path.of(properties.getTmp(), captureId, "front.jpg");
        Path expectedBack = Path.of(properties.getTmp(), captureId, "back.png");

        assertEquals(List.of(expectedFront.toString(), expectedBack.toString()), result);
        assertTrue(Files.exists(expectedFront));
        assertTrue(Files.exists(expectedBack));
        assertEquals("nested/front.jpg", Files.readString(expectedFront));
        assertEquals("back.png", Files.readString(expectedBack));
    }

    @Test
    @DisplayName("Given processed zip When moving to backup Then should move file to dated backup folder")
    void shouldMoveZipToBackupFolder() throws IOException {
        String captureId = "capture-backup";
        Path zipPath = createZip("capture.jpg");

        extractor.moveToBackup(zipPath, captureId);

        Path expectedPath = datedPath(properties.getBackup(), captureId);
        assertTrue(Files.exists(expectedPath));
        assertFalse(Files.exists(zipPath));
    }

    @Test
    @DisplayName("Given failed zip When moving to error Then should move file to dated error folder")
    void shouldMoveZipToErrorFolder() throws IOException {
        String captureId = "capture-error";
        Path zipPath = createZip("capture.jpg");

        extractor.moveToError(zipPath, captureId);

        Path expectedPath = datedPath(properties.getError(), captureId);
        assertTrue(Files.exists(expectedPath));
        assertFalse(Files.exists(zipPath));
    }

    @Test
    @DisplayName("Given missing zip When moving to error Then should throw CaptureNotFoundException")
    void shouldThrowWhenMovingMissingZipToError() {
        Path zipPath = tempDir.resolve("missing.zip");

        CaptureNotFoundException exception = assertThrows(
                CaptureNotFoundException.class,
                () -> extractor.moveToError(zipPath, "missing")
        );

        assertTrue(exception.getMessage().contains("ZIP filename not found"));
    }

    private Path createZip(String... entries) throws IOException {
        Path zipPath = Files.createTempFile(tempDir, "capture-", ".zip");

        try (ZipOutputStream outputStream = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            for (String entryName : entries) {
                ZipEntry entry = new ZipEntry(entryName);
                outputStream.putNextEntry(entry);
                outputStream.write(entryName.getBytes());
                outputStream.closeEntry();
            }
        }

        return zipPath;
    }

    private Path datedPath(String root, String captureId) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return Path.of(root, today, captureId + ".zip");
    }
}
