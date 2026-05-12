package com.arthurscarpin.acs.infrastructure.mapper;

import com.arthurscarpin.acs.core.capture.domain.Capture;
import com.arthurscarpin.acs.core.capture.domain.CaptureImage;
import com.arthurscarpin.acs.core.capture.domain.CaptureOCR;
import com.arthurscarpin.acs.core.capture.domain.CaptureOCRStatus;
import com.arthurscarpin.acs.infrastructure.persistence.entity.document.CaptureEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.document.CaptureImageEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.document.CaptureOCREntity;
import com.arthurscarpin.acs.infrastructure.presentation.request.CaptureOCRStatusRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CaptureMapper {

    CaptureEntity fromDomainToEntity(Capture capture);
    Capture fromEntityToDomain(CaptureEntity captureEntity);

    List<CaptureImageEntity> toImageEntityList(List<CaptureImage> images);

    CaptureImageEntity toImageEntity(CaptureImage image);

    CaptureOCREntity toOCREntity(CaptureOCR ocr);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "processedImagesCount", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDomain(Capture domain, @MappingTarget CaptureEntity entity);

    CaptureOCRStatus toOCRStatus(CaptureOCRStatusRequest request);
}
