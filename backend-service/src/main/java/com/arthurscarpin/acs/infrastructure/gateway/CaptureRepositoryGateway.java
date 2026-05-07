package com.arthurscarpin.acs.infrastructure.gateway;

import com.arthurscarpin.acs.core.capture.domain.Capture;
import com.arthurscarpin.acs.core.capture.domain.CaptureMessage;
import com.arthurscarpin.acs.core.capture.gateway.CaptureGateway;
import com.arthurscarpin.acs.infrastructure.mapper.CaptureMapper;
import com.arthurscarpin.acs.infrastructure.persistence.entity.document.CaptureEntity;
import com.arthurscarpin.acs.infrastructure.persistence.repository.document.CaptureRepository;
import com.arthurscarpin.acs.infrastructure.presentation.producer.CaptureProducer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

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
}
