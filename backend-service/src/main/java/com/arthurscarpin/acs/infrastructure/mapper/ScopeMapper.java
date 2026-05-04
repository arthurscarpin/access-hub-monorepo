package com.arthurscarpin.acs.infrastructure.mapper;

import com.arthurscarpin.acs.core.scope.domain.Scope;
import com.arthurscarpin.acs.infrastructure.persistence.entity.ScopeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScopeMapper {

    Scope fromEntityToDomain(ScopeEntity entity);
}
