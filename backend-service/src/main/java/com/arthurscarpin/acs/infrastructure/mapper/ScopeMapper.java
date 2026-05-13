package com.arthurscarpin.acs.infrastructure.mapper;

import com.arthurscarpin.acs.core.scope.domain.Scope;
import com.arthurscarpin.acs.infrastructure.persistence.entity.table.ScopeEntity;
import com.arthurscarpin.acs.infrastructure.presentation.response.ScopeResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScopeMapper {

    Scope fromEntityToDomain(ScopeEntity entity);

    ScopeResponse fromDomainToResponse(Scope scope);
}
