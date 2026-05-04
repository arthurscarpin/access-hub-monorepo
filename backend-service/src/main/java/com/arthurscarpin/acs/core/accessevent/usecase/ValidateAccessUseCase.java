package com.arthurscarpin.acs.core.accessevent.usecase;

import com.arthurscarpin.acs.core.accessevent.domain.AccessEvent;

public interface ValidateAccessUseCase {

    AccessEvent execute(AccessEvent accessEvent);
}
