package com.arthurscarpin.acs.core.owner.usecase;

import com.arthurscarpin.acs.core.owner.domain.Owner;
import com.arthurscarpin.acs.core.owner.gateway.OwnerGateway;
import com.arthurscarpin.acs.core.pagination.PageInput;
import com.arthurscarpin.acs.core.pagination.PageOutput;

public class GetOwnersUseCaseImpl implements GetOwnersUseCase {

    private final OwnerGateway gateway;

    public GetOwnersUseCaseImpl(OwnerGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public PageOutput<Owner> execute(PageInput pageInput) {
        return gateway.findByFilters(pageInput);
    }
}
