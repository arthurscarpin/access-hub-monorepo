package com.arthurscarpin.acs.infrastructure.gateway;

import com.arthurscarpin.acs.core.accessevent.domain.AccessEvent;
import com.arthurscarpin.acs.core.accessevent.gateway.AccessEventGateway;
import com.arthurscarpin.acs.core.pagination.PageInput;
import com.arthurscarpin.acs.core.pagination.PageOutput;
import com.arthurscarpin.acs.infrastructure.mapper.AccessEventMapper;
import com.arthurscarpin.acs.infrastructure.persistence.entity.AccessEventEntity;
import com.arthurscarpin.acs.infrastructure.persistence.repository.AccessEventRepository;
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

@Component
@RequiredArgsConstructor
public class AccessEventRepositoryGateway implements AccessEventGateway {

    private final AccessEventRepository repository;

    private final AccessEventMapper mapper;

    @Transactional
    @Override
    public AccessEvent save(AccessEvent domain) {
        AccessEventEntity entity = mapper.fromDomainToEntity(domain);
        return mapper.fromEntityToDomain(repository.save(entity));
    }

    @Override
    public PageOutput<AccessEvent> findByFilters(PageInput pageInput) {
        Pageable pageable = PageRequest.of(
                pageInput.pageNumber(),
                pageInput.pageSize(),
                Sort.by(Sort.Direction.DESC, "timestamp")
        );

        Specification<AccessEventEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (pageInput.plate() != null && !pageInput.plate().isBlank()) {
                predicates.add(cb.equal(root.get("plate"), pageInput.plate()));
            }

            if (pageInput.from() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), pageInput.from()));
            }

            if (pageInput.to() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), pageInput.to()));
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<AccessEventEntity> page = repository.findAll(spec, pageable);

        List<AccessEvent> content = page.getContent().stream()
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