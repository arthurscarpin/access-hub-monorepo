package com.arthurscarpin.acs.infrastructure.mapper;

import com.arthurscarpin.acs.core.owner.domain.Owner;
import com.arthurscarpin.acs.infrastructure.presentation.request.OwnerRequest;
import com.arthurscarpin.acs.infrastructure.presentation.response.OwnerResponse;
import com.arthurscarpin.acs.infrastructure.persistence.entity.OwnerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OwnerMapper {

    Owner fromRequestToDomain(OwnerRequest ownerRequest);

    OwnerResponse fromDomainToResponse(Owner owner);

    OwnerEntity fromDomainToEntity(Owner owner);

    Owner fromEntityToDomain(OwnerEntity entity);
}
