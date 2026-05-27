package com.arthurscarpin.acs.core.owner.usecase;

import com.arthurscarpin.acs.core.owner.domain.Owner;
import com.arthurscarpin.acs.core.pagination.PageInput;
import com.arthurscarpin.acs.core.pagination.PageOutput;

public interface GetOwnersUseCase {

    PageOutput<Owner> execute(PageInput pageInput);
}
