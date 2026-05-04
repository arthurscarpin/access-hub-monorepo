package com.arthurscarpin.acs.core.user.usecase;

import com.arthurscarpin.acs.core.user.domain.User;

public interface RegisterUserUseCase {

    User execute(User user);
}
