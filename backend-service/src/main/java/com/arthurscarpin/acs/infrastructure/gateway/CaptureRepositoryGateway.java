package com.arthurscarpin.acs.infrastructure.gateway;

import com.arthurscarpin.acs.core.capture.domain.Capture;
import com.arthurscarpin.acs.core.capture.domain.CaptureMessage;
import com.arthurscarpin.acs.core.capture.domain.ImageStatus;
import com.arthurscarpin.acs.core.capture.exception.CaptureNotFoundException;
import com.arthurscarpin.acs.core.capture.gateway.CaptureGateway;
import com.arthurscarpin.acs.infrastructure.mapper.CaptureMapper;
import com.arthurscarpin.acs.infrastructure.persistence.entity.document.CaptureEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.document.CaptureImageEntity;
import com.arthurscarpin.acs.infrastructure.persistence.repository.document.CaptureRepository;
import com.arthurscarpin.acs.infrastructure.presentation.producer.AIProducer;
import com.arthurscarpin.acs.infrastructure.presentation.producer.CaptureProducer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CaptureRepositoryGateway implements CaptureGateway {

    private final CaptureRepository repository;

    private final CaptureProducer captureProducer;

    private final AIProducer iAProducer;

    private final CaptureMapper mapper;

    @Transactional
    @Override
    public Capture saveAndPublish(Capture domain) {
        log.info("Starting persistence and publishing process for new capture.");
        log.debug("Capture data received: {}", domain);

        CaptureEntity entity = mapper.fromDomainToEntity(domain);
        CaptureEntity savedEntity = repository.save(entity);
        Capture savedDomain = mapper.fromEntityToDomain(savedEntity);

        log.info("Capture saved successfully with ID: {}. Preparing to publish {} images.",
                savedDomain.id(), savedDomain.images().size());

        savedDomain.images().forEach(image -> {
            CaptureMessage m = new CaptureMessage(savedDomain.id(), image.id(), image.filename(), Instant.now());
            log.debug("Publishing message to producer: [CaptureID: {}, ImageID: {}]", m.captureId(), m.imageId());
            captureProducer.publish(m);
        });

        return savedDomain;
    }

    @Override
    public Capture findByCaptureIdAndImageId(String captureId, String imageId) {
        log.debug("Searching for capture with ID: {} and image ID: {}", captureId, imageId);

        CaptureEntity entity = repository.findByIdAndImages_Id(captureId, imageId)
                .orElseThrow(() -> {
                    log.warn("Capture not found for IDs: Capture [{}], Image [{}]", captureId, imageId);
                    return new CaptureNotFoundException("Capture not found");
                });

        return mapper.fromEntityToDomain(entity);
    }

    @Transactional
    @Override
    public void updateAndPublish(Capture domain) {
        log.info("Updating capture ID: {}", domain.id());

        CaptureEntity entity = repository.findById(domain.id())
                .orElseThrow(() -> {
                    log.error("Update failed: Capture with ID {} does not exist.", domain.id());
                    return new CaptureNotFoundException("Capture not found");
                });

        mapper.updateEntityFromDomain(domain, entity);

        List<CaptureImageEntity> imageEntities = mapper.toImageEntityList(domain.images());
        entity.setImages(imageEntities);

        long completedCount = imageEntities.stream()
                .filter(img -> img.getStatus() == ImageStatus.COMPLETED ||
                        img.getStatus() == ImageStatus.FAILED)
                .count();

        entity.setProcessedImagesCount((int) completedCount);

        log.debug("Internal update state - Capture: {}, Processed images count: {}", domain.id(), completedCount);

        CaptureEntity updatedEntity = repository.save(entity);
        log.info("Capture ID: {} successfully updated.", domain.id());

        Capture updatedDomain = mapper.fromEntityToDomain(updatedEntity);

        if (updatedDomain.processedImagesCount() >= updatedDomain.images().size()) {
            iAProducer.publish(updatedDomain);
        }
    }
}