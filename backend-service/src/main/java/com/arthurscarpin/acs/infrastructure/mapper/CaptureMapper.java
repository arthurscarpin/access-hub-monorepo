package com.arthurscarpin.acs.infrastructure.mapper;

import com.arthurscarpin.acs.core.capture.domain.Capture;
import com.arthurscarpin.acs.infrastructure.persistence.entity.CaptureEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CaptureMapper {

    CaptureEntity fromDomainToEntity(Capture capture);

    Capture fromEntityToDomain(CaptureEntity captureEntity);
}
