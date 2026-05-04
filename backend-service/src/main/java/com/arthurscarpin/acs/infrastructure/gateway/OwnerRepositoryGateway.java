package com.arthurscarpin.acs.infrastructure.gateway;

import com.arthurscarpin.acs.core.owner.domain.DocumentType;
import com.arthurscarpin.acs.core.owner.domain.Owner;
import com.arthurscarpin.acs.core.owner.gateway.OwnerGateway;
import com.arthurscarpin.acs.infrastructure.mapper.OwnerMapper;
import com.arthurscarpin.acs.infrastructure.persistence.entity.OwnerEntity;
import com.arthurscarpin.acs.infrastructure.persistence.repository.OwnerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OwnerRepositoryGateway implements OwnerGateway {

    private final OwnerRepository repository;

    private final OwnerMapper mapper;

    @Override
    public Optional<Owner> findById(UUID uuid) {
        return repository.findById(uuid)
                .map(mapper::fromEntityToDomain);
    }

    @Override
    public Optional<Owner> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(mapper::fromEntityToDomain);
    }

    @Override
    public Optional<Owner> findByDocumentAndDocumentType(String document, DocumentType documentType) {
        return repository.findByDocumentAndDocumentType(document, documentType)
                .map(mapper::fromEntityToDomain);
    }

    @Transactional
    @Override
    public Owner save(Owner domain) {
        OwnerEntity entity = mapper.fromDomainToEntity(domain);
        return mapper.fromEntityToDomain(repository.save(entity));
    }
}
