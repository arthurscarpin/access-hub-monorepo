package com.arthurscarpin.acs.core.accessevent.usecase;

import com.arthurscarpin.acs.core.accessevent.domain.AccessEvent;
import com.arthurscarpin.acs.core.accessevent.gateway.AccessEventGateway;
import com.arthurscarpin.acs.core.pagination.PageInput;
import com.arthurscarpin.acs.core.pagination.PageOutput;

public class GetAccessHistoryUseCaseImpl implements GetAccessHistoryUseCase {

    private final AccessEventGateway gateway;

    public GetAccessHistoryUseCaseImpl(AccessEventGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public PageOutput<AccessEvent> execute(PageInput pageInput) {
        return gateway.findByFilters(pageInput);
    }
}
