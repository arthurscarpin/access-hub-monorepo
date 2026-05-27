package com.arthurscarpin.acs.core.user.usecase;

import com.arthurscarpin.acs.core.pagination.PageInput;
import com.arthurscarpin.acs.core.pagination.PageOutput;
import com.arthurscarpin.acs.core.user.domain.User;

public interface GetUsersUseCase {

    PageOutput<User> execute(PageInput pageInput);
}
