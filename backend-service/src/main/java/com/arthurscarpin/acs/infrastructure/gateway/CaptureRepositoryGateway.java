package com.arthurscarpin.acs.infrastructure.gateway;

import com.arthurscarpin.acs.core.capture.domain.Capture;
import com.arthurscarpin.acs.core.capture.gateway.CaptureGateway;
import com.arthurscarpin.acs.infrastructure.mapper.CaptureMapper;
import com.arthurscarpin.acs.infrastructure.persistence.entity.document.CaptureEntity;
import com.arthurscarpin.acs.infrastructure.persistence.repository.document.CaptureRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CaptureRepositoryGateway implements CaptureGateway {

    private final CaptureRepository repository;

    private final CaptureMapper mapper;

    @Transactional
    @Override
    public Capture save(Capture domain) {
        CaptureEntity entity = mapper.fromDomainToEntity(domain);
        return mapper.fromEntityToDomain(repository.save(entity));
    }
}
