package com.arthurscarpin.acs.infrastructure.mapper;

import com.arthurscarpin.acs.core.user.domain.User;
import com.arthurscarpin.acs.infrastructure.presentation.request.LoginRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoginMapper {

    User fromRequestToDomain(LoginRequest loginRequest);
}
