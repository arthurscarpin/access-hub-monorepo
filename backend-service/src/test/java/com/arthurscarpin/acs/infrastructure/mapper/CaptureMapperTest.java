package com.arthurscarpin.acs.infrastructure.mapper;

import com.arthurscarpin.acs.core.capture.domain.*;
import com.arthurscarpin.acs.infrastructure.persistence.entity.document.CaptureEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.document.CaptureImageEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CaptureMapperTest {

    private final CaptureMapper mapper = Mappers.getMapper(CaptureMapper.class);

    @Test
    @DisplayName("Given Capture domain When mapping to entity Then should return CaptureEntity")
    void shouldMapCaptureDomainToEntity() {
        Instant now = Instant.now();
        CaptureImage image = new CaptureImage(
                "image-1",
                "plate.jpg",
                "ABC-1234",
                95.5,
                ImageStatus.COMPLETED,
                now
        );

        Capture domain = new Capture(
                "capture-1",
                List.of(image),
                CaptureStatus.PROCESSING,
                "ABC-1234",
                95.5,
                now,
                now,
                now
        );

        CaptureEntity result = mapper.fromDomainToEntity(domain);

        assertNotNull(result);
        assertEquals(domain.id(), result.getId());
        assertEquals(domain.status(), result.getStatus());
        assertEquals(domain.finalPlate(), result.getFinalPlate());
        assertEquals(domain.finalConfidence(), result.getFinalConfidence());
        assertEquals(domain.createdAt(), result.getCreatedAt());
        assertEquals(domain.updatedAt(), result.getUpdatedAt());
        assertEquals(domain.processedAt(), result.getProcessedAt());

        assertNotNull(result.getImages());
        assertEquals(1, result.getImages().size());
        CaptureImageEntity imageEntity = result.getImages().get(0);
        assertEquals(image.id(), imageEntity.getId());
        assertEquals(image.filename(), imageEntity.getFilename());
        assertEquals(image.ocrText(), imageEntity.getOcrText());
        assertEquals(image.confidence(), imageEntity.getConfidence());
        assertEquals(image.status(), imageEntity.getStatus());
        assertEquals(image.timestamp(), imageEntity.getTimestamp());
    }

    @Test
    @DisplayName("Given null Capture domain When mapping to entity Then should return null")
    void shouldReturnNullWhenCaptureDomainIsNull() {
        assertNull(mapper.fromDomainToEntity(null));
    }

    @Test
    @DisplayName("Given CaptureEntity When mapping to domain Then should return Capture")
    void shouldMapCaptureEntityToDomain() {
        Instant now = Instant.now();
        CaptureImageEntity imageEntity = CaptureImageEntity.builder()
                .id("image-1")
                .filename("plate.jpg")
                .ocrText("ABC-1234")
                .confidence(95.5)
                .status(ImageStatus.COMPLETED)
                .timestamp(now)
                .build();

        CaptureEntity entity = CaptureEntity.builder()
                .id("capture-1")
                .images(List.of(imageEntity))
                .status(CaptureStatus.PROCESSING)
                .finalPlate("ABC-1234")
                .finalConfidence(95.5)
                .createdAt(now)
                .updatedAt(now)
                .processedAt(now)
                .build();

        Capture result = mapper.fromEntityToDomain(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.id());
        assertEquals(entity.getStatus(), result.status());
        assertEquals(entity.getFinalPlate(), result.finalPlate());
        assertEquals(entity.getFinalConfidence(), result.finalConfidence());
        assertEquals(entity.getCreatedAt(), result.createdAt());
        assertEquals(entity.getUpdatedAt(), result.updatedAt());
        assertEquals(entity.getProcessedAt(), result.processedAt());

        assertNotNull(result.images());
        assertEquals(1, result.images().size());
        CaptureImage image = result.images().get(0);
        assertEquals(imageEntity.getId(), image.id());
        assertEquals(imageEntity.getFilename(), image.filename());
        assertEquals(imageEntity.getOcrText(), image.ocrText());
        assertEquals(imageEntity.getConfidence(), image.confidence());
        assertEquals(imageEntity.getStatus(), image.status());
        assertEquals(imageEntity.getTimestamp(), image.timestamp());
    }

    @Test
    @DisplayName("Given null CaptureEntity When mapping to domain Then should return null")
    void shouldReturnNullWhenCaptureEntityIsNull() {
        assertNull(mapper.fromEntityToDomain(null));
    }
}