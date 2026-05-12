package com.arthurscarpin.acs.infrastructure.mapper;

import com.arthurscarpin.acs.core.capture.domain.*;
import com.arthurscarpin.acs.infrastructure.persistence.entity.document.CaptureEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.document.CaptureImageEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.document.CaptureOCREntity;
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

        CaptureOCR ocrDomain = new CaptureOCR("ABC-1234", 95.5, List.of(List.of(1, 2), List.of(3, 4)));

        CaptureImage image = new CaptureImage(
                "image-1",
                "plate.jpg",
                ImageStatus.COMPLETED,
                List.of(ocrDomain),
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
                now,
                1,
                1L
        );

        CaptureEntity result = mapper.fromDomainToEntity(domain);

        assertNotNull(result);
        assertEquals(domain.id(), result.getId());
        assertEquals(domain.status(), result.getStatus());
        assertEquals(domain.finalPlate(), result.getFinalPlate());
        assertEquals(domain.finalConfidence(), result.getFinalConfidence());
        assertEquals(domain.processedImagesCount(), result.getProcessedImagesCount());
        assertEquals(domain.version(), result.getVersion());

        assertNotNull(result.getImages());
        CaptureImageEntity imageEntity = result.getImages().get(0);
        assertEquals(image.id(), imageEntity.getId());
        assertNotNull(imageEntity.getOcr());
        assertEquals(1, imageEntity.getOcr().size());
        assertEquals(ocrDomain.text(), imageEntity.getOcr().get(0).getText());
        assertEquals(ocrDomain.confidence(), imageEntity.getOcr().get(0).getConfidence());
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

        CaptureOCREntity ocrEntity = CaptureOCREntity.builder()
                .text("ABC-1234")
                .confidence(95.5)
                .bbox(List.of(List.of(0, 0)))
                .build();

        CaptureImageEntity imageEntity = CaptureImageEntity.builder()
                .id("image-1")
                .filename("plate.jpg")
                .status(ImageStatus.COMPLETED)
                .ocr(List.of(ocrEntity))
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
                .processedImagesCount(1)
                .version(1L)
                .build();

        Capture result = mapper.fromEntityToDomain(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.id());
        assertEquals(entity.getVersion(), result.version());
        assertEquals(entity.getProcessedImagesCount(), result.processedImagesCount());

        assertNotNull(result.images());
        CaptureImage imageDomain = result.images().get(0);
        assertEquals(imageEntity.getId(), imageDomain.id());
        assertNotNull(imageDomain.ocr());
        assertEquals(1, imageDomain.ocr().size());
        assertEquals(ocrEntity.getText(), imageDomain.ocr().get(0).text());
    }

    @Test
    @DisplayName("Given null CaptureEntity When mapping to domain Then should return null")
    void shouldReturnNullWhenCaptureEntityIsNull() {
        assertNull(mapper.fromEntityToDomain(null));
    }
}