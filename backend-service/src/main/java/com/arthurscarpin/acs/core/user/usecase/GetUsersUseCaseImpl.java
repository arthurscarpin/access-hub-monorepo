package com.arthurscarpin.acs.core.user.usecase;

import com.arthurscarpin.acs.core.pagination.PageInput;
import com.arthurscarpin.acs.core.pagination.PageOutput;
import com.arthurscarpin.acs.core.user.domain.User;
import com.arthurscarpin.acs.core.user.gateway.UserGateway;

public class GetUsersUseCaseImpl implements GetUsersUseCase {

    private final UserGateway gateway;

    public GetUsersUseCaseImpl(UserGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public PageOutput<User> execute(PageInput pageInput) {
        return gateway.findByFilters(pageInput);
    }
}
