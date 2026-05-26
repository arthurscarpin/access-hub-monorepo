package com.arthurscarpin.acs.infrastructure.gateway;

import com.arthurscarpin.acs.core.accessevent.domain.AccessEvent;
import com.arthurscarpin.acs.core.owner.domain.DocumentType;
import com.arthurscarpin.acs.core.owner.domain.Owner;
import com.arthurscarpin.acs.core.owner.gateway.OwnerGateway;
import com.arthurscarpin.acs.core.pagination.PageInput;
import com.arthurscarpin.acs.core.pagination.PageOutput;
import com.arthurscarpin.acs.infrastructure.mapper.OwnerMapper;
import com.arthurscarpin.acs.infrastructure.persistence.entity.table.AccessEventEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.table.OwnerEntity;
import com.arthurscarpin.acs.infrastructure.persistence.repository.table.OwnerRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public PageOutput<Owner> findByFilters(PageInput pageInput) {
        Pageable pageable = PageRequest.of(
                pageInput.pageNumber(),
                pageInput.pageSize(),
                Sort.by(Sort.Direction.ASC, "name")
        );

        Page<OwnerEntity> page = repository.findAll(pageable);

        List<Owner> content = page.getContent().stream()
                .map(mapper::fromEntityToDomain)
                .toList();

        return new PageOutput<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
