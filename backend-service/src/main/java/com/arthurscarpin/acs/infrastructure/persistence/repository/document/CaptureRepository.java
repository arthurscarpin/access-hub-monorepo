package com.arthurscarpin.acs.infrastructure.persistence.repository.document;

import com.arthurscarpin.acs.infrastructure.persistence.entity.document.CaptureEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaptureRepository extends MongoRepository<CaptureEntity, String> {

    Optional<CaptureEntity> findByIdAndImages_Id(String id, String imageId);
}
