package com.arthurscarpin.acs.infrastructure.gateway;

import com.arthurscarpin.acs.core.capture.domain.Capture;
import com.arthurscarpin.acs.core.capture.domain.CaptureImage;
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

    @Override
    public Capture findById(String captureId) {
        CaptureEntity entity = repository.findById(captureId)
                .orElseThrow(() -> {
            log.warn("Capture not found: Capture [{}]", captureId);
            return new CaptureNotFoundException("Capture not found");
        });
        return mapper.fromEntityToDomain(entity);
    }

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

    @Transactional
    @Override
    public void updateAndPublish(Capture domain, String processedImageId) {
        CaptureImage processedImage = domain.images().stream()
                .filter(img -> img.id().equals(processedImageId))
                .findFirst()
                .orElse(null);

        if (processedImage == null) {
            log.warn("Image ID {} not found in domain for Capture ID: {}", processedImageId, domain.id());
            return;
        }

        CaptureImageEntity imageEntity = mapper.toImageEntity(processedImage);

        CaptureEntity updatedEntity = repository.incrementProcessedCountAndUpsertSingleImage(
                domain.id(),
                processedImageId,
                imageEntity
        );

        if (updatedEntity == null) {
            log.info("The image {} has already been processed. Ignoring to prevent duplication.", processedImageId);
            return;
        }

        int totalImages = updatedEntity.getImages().size();
        log.info("Mongo Atomic Update - Capture ID: {} | Processed: {}/{}",
                domain.id(), updatedEntity.getProcessedImagesCount(), totalImages);

        if (updatedEntity.getProcessedImagesCount() == totalImages) {
            log.info("Success! This thread atomically processed the last image. Forcing fresh database reload...");

            CaptureEntity freshEntity = repository.findById(domain.id())
                    .orElseThrow(() -> new CaptureNotFoundException(
                            "Capture not found for publishing: " + domain.id()
                    ));

            Capture updatedDomain = mapper.fromEntityToDomain(freshEntity);

            log.info("Publishing consolidated payload with all extracted OCRs to RabbitMQ...");
            iAProducer.publish(updatedDomain);
        }
    }

    @Override
    public Capture update(Capture domain) {
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

        return mapper.fromEntityToDomain(updatedEntity);
    }
}