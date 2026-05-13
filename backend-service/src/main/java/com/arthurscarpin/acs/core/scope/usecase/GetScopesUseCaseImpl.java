package com.arthurscarpin.acs.core.scope.usecase;

import com.arthurscarpin.acs.core.scope.domain.Scope;
import com.arthurscarpin.acs.core.scope.gateway.ScopeGateway;

import java.util.List;

public class GetScopesUseCaseImpl implements GetScopesUseCase {

    private final ScopeGateway gateway;

    public GetScopesUseCaseImpl(ScopeGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public List<Scope> execute() {
        return gateway.findAll();
    }
}
