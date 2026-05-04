package com.arthurscarpin.acs.infrastructure.mapper;

import com.arthurscarpin.acs.core.user.domain.User;
import com.arthurscarpin.acs.infrastructure.persistence.entity.ScopeEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.UserEntity;
import com.arthurscarpin.acs.infrastructure.presentation.request.UserRequest;
import com.arthurscarpin.acs.infrastructure.presentation.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromRequestToDomain(UserRequest userRequest);

    UserResponse fromDomainToResponse(User user);

    @Mapping(target = "scopes", ignore = true)
    UserEntity fromDomainToEntity(User user);

    @Mapping(target = "scopes", source = "scopes")
    User fromEntityToDomain(UserEntity entity);

    default UUID map(ScopeEntity scope) {
        return scope != null ? scope.getId() : null;
    }

    default List<UUID> map(List<ScopeEntity> scopes) {
        return scopes == null
                ? List.of()
                : scopes.stream().map(this::map).toList();
    }
}
