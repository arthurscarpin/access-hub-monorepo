package com.arthurscarpin.acs.infrastructure.mapper;

import com.arthurscarpin.acs.core.accessevent.domain.AccessEvent;
import com.arthurscarpin.acs.infrastructure.persistence.entity.AccessEventEntity;
import com.arthurscarpin.acs.infrastructure.presentation.request.AccessEventRequest;
import com.arthurscarpin.acs.infrastructure.presentation.response.AccessEventResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccessEventMapper {

    AccessEvent fromRequestToDomain(AccessEventRequest accessEventRequest);

    AccessEventResponse fromDomainToResponse(AccessEvent accessEvent);

    AccessEventEntity fromDomainToEntity(AccessEvent accessEvent);

    AccessEvent fromEntityToDomain(AccessEventEntity accessEventEntity);
}
