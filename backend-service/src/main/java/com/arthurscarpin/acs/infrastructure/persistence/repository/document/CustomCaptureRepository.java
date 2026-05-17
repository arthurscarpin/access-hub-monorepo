package com.arthurscarpin.acs.infrastructure.persistence.repository.document;

import com.arthurscarpin.acs.infrastructure.persistence.entity.document.CaptureEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.document.CaptureImageEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomCaptureRepository {

    CaptureEntity incrementProcessedCountAndUpsertSingleImage(String id, String imageId, CaptureImageEntity image);
}
