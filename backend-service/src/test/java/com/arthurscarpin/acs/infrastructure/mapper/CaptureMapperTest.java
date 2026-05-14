package com.arthurscarpin.acs.infrastructure.mapper;

import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import com.arthurscarpin.acs.core.capture.domain.*;
import com.arthurscarpin.acs.infrastructure.persistence.entity.document.CaptureEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.document.CaptureImageEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.document.CaptureOCREntity;
import com.arthurscarpin.acs.infrastructure.presentation.request.CaptureOCRStatusRequest;
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
        List<List<Integer>> bbox = List.of(List.of(10, 10), List.of(50, 50));
        CaptureOCR ocrDomain = new CaptureOCR("ABC-1234", 95.5, bbox);

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
                Direction.IN,
                "ABC-1234",
                95.5,
                "AI analysis",
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

        CaptureImageEntity imageEntity = result.getImages().get(0);
        assertEquals(ocrDomain.bbox(), imageEntity.getOcr().get(0).getBbox());
    }

    @Test
    @DisplayName("Given CaptureEntity When mapping to domain Then should return Capture")
    void shouldMapCaptureEntityToDomain() {
        Instant now = Instant.now();
        List<List<Integer>> bbox = List.of(List.of(0, 0), List.of(100, 100));

        CaptureOCREntity ocrEntity = CaptureOCREntity.builder()
                .text("XYZ-9999")
                .confidence(80.0)
                .bbox(bbox)
                .build();

        CaptureImageEntity imageEntity = CaptureImageEntity.builder()
                .id("img-99")
                .status(ImageStatus.STARTED)
                .ocr(List.of(ocrEntity))
                .timestamp(now)
                .build();

        CaptureEntity entity = CaptureEntity.builder()
                .id("cap-99")
                .images(List.of(imageEntity))
                .status(CaptureStatus.RECEIVED)
                .version(2L)
                .build();

        Capture result = mapper.fromEntityToDomain(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.id());
        assertEquals(bbox, result.images().get(0).ocr().get(0).bbox());
        assertEquals(ImageStatus.STARTED, result.images().get(0).status());
    }

    @Test
    @DisplayName("Given CaptureOCRStatusRequest When mapping to domain Then should return CaptureOCRStatus")
    void shouldMapRequestToOCRStatusDomain() {
        List<CaptureOCR> ocrList = List.of(new CaptureOCR("TEST", 1.0, List.of(List.of(1,1))));
        CaptureOCRStatusRequest request = new CaptureOCRStatusRequest(
                "cap-123",
                "img-456",
                "file.jpg",
                Instant.now(),
                ImageStatus.COMPLETED,
                CaptureStatus.COMPLETED,
                "Success",
                ocrList
        );

        CaptureOCRStatus result = mapper.toOCRStatus(request);

        assertNotNull(result);
        assertEquals(request.captureId(), result.captureId());
        assertEquals(request.imageStatus(), result.imageStatus());
        assertEquals(request.ocr().size(), result.ocr().size());
        assertEquals(request.ocr().get(0).text(), result.ocr().get(0).text());
    }

    @Test
    @DisplayName("Given domain When updateEntityFromDomain Then should update existing entity ignoring specific fields")
    void shouldUpdateEntityFromDomain() {
        CaptureEntity entity = CaptureEntity.builder()
                .id("original-id")
                .version(1L)
                .processedImagesCount(5)
                .status(CaptureStatus.RECEIVED)
                .build();

        Capture domain = new Capture(
                "new-id",
                List.of(),
                CaptureStatus.COMPLETED,
                Direction.IN,
                "NEW-123",
                100.0,
                "IA Analysis",
                Instant.now(),
                Instant.now(),
                Instant.now(),
                10,
                2L
        );

        mapper.updateEntityFromDomain(domain, entity);

        assertEquals("original-id", entity.getId());
        assertEquals(1L, entity.getVersion());
        assertEquals(5, entity.getProcessedImagesCount());
        assertEquals(CaptureStatus.COMPLETED, entity.getStatus());
        assertEquals("NEW-123", entity.getFinalPlate());
    }

    @Test
    @DisplayName("Given null inputs When mapping Then should return null")
    void shouldHandleNullInputs() {
        assertNull(mapper.fromDomainToEntity(null));
        assertNull(mapper.fromEntityToDomain(null));
        assertNull(mapper.toOCRStatus(null));
        assertNull(mapper.toImageEntity(null));
    }
}