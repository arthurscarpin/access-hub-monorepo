package com.arthurscarpin.acs.core.scope.gateway;

import com.arthurscarpin.acs.core.scope.domain.Scope;

import java.util.List;
import java.util.UUID;

public interface ScopeGateway {

    List<UUID> findAllIdsByIds(List<UUID> scopes);

    List<Scope> findAllByIdIn(List<UUID> scopes);
}
