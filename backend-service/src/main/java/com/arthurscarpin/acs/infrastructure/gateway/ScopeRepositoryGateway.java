package com.arthurscarpin.acs.infrastructure.gateway;

import com.arthurscarpin.acs.core.scope.domain.Scope;
import com.arthurscarpin.acs.core.scope.gateway.ScopeGateway;
import com.arthurscarpin.acs.infrastructure.mapper.ScopeMapper;
import com.arthurscarpin.acs.infrastructure.persistence.repository.ScopeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ScopeRepositoryGateway implements ScopeGateway {

    private final ScopeRepository repository;

    private final ScopeMapper mapper;

    @Override
    public List<UUID> findAllIdsByIds(List<UUID> scopes) {
        return repository.findAllIdsByIds(scopes);
    }

    @Override
    public List<Scope> findAllByIdIn(List<UUID> scopes) {
        return repository.findAllByIdIn(scopes)
                .stream()
                .map(mapper::fromEntityToDomain)
                .toList();
    }
}
