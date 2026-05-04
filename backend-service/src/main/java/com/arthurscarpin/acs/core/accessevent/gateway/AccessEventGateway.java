package com.arthurscarpin.acs.core.accessevent.gateway;

import com.arthurscarpin.acs.core.accessevent.domain.AccessEvent;
import com.arthurscarpin.acs.core.pagination.PageInput;
import com.arthurscarpin.acs.core.pagination.PageOutput;

public interface AccessEventGateway {

    AccessEvent save(AccessEvent accessEvent);

    PageOutput<AccessEvent> findByFilters(PageInput pageInput);
}
