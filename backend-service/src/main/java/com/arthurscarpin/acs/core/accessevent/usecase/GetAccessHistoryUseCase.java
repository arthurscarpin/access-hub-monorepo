package com.arthurscarpin.acs.core.accessevent.usecase;

import com.arthurscarpin.acs.core.accessevent.domain.AccessEvent;
import com.arthurscarpin.acs.core.pagination.PageInput;
import com.arthurscarpin.acs.core.pagination.PageOutput;


public interface GetAccessHistoryUseCase {

    PageOutput<AccessEvent> execute(PageInput pageInput);
}
