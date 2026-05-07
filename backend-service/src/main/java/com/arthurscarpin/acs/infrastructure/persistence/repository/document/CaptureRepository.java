package com.arthurscarpin.acs.infrastructure.persistence.repository.document;

import com.arthurscarpin.acs.infrastructure.persistence.entity.document.CaptureEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CaptureRepository extends MongoRepository<CaptureEntity, UUID> {
}
