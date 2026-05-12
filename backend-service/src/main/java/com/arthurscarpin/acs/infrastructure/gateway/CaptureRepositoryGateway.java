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
import com.arthurscarpin.acs.infrastructure.presentation.producer.CaptureProducer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CaptureRepositoryGateway implements CaptureGateway {

    private final CaptureRepository repository;

    private final CaptureProducer producer;

    private final CaptureMapper mapper;

    @Transactional
    @Override
    public Capture saveAndPublish(Capture domain) {
        CaptureEntity entity = mapper.fromDomainToEntity(domain);
        Capture savedDomain = mapper.fromEntityToDomain(repository.save(entity));
        savedDomain.images().forEach(
                image -> {
                    CaptureMessage m = new CaptureMessage(savedDomain.id(), image.id(), image.filename(), Instant.now());
                    producer.publish(m);
                }
        );
        return savedDomain;
    }

    @Override
    public Capture findByCaptureIdAndImageId(String captureId, String imageId) {
        CaptureEntity entity = repository.findByIdAndImages_Id(captureId, imageId)
                .orElseThrow(() -> new CaptureNotFoundException("Capture not found"));
        return mapper.fromEntityToDomain(entity);
    }

    @Transactional
    @Override
    public Capture update(Capture domain) {
        CaptureEntity entity = repository.findById(domain.id())
                .orElseThrow(() -> new CaptureNotFoundException("Capture not found"));
        mapper.updateEntityFromDomain(domain, entity);

        List<CaptureImageEntity> imageEntities = mapper.toImageEntityList(domain.images());
        entity.setImages(imageEntities);

        long completedCount = imageEntities.stream()
                .filter(img -> img.getStatus() == ImageStatus.COMPLETED ||
                        img.getStatus() == ImageStatus.FAILED)
                .count();

        entity.setProcessedImagesCount((int) completedCount);
        return mapper.fromEntityToDomain(repository.save(entity));
    }
}
