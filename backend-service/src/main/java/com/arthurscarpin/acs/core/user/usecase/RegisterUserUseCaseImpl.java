package com.arthurscarpin.acs.core.user.usecase;

import com.arthurscarpin.acs.core.scope.exception.ScopeNotFoundException;
import com.arthurscarpin.acs.core.scope.gateway.ScopeGateway;
import com.arthurscarpin.acs.core.user.domain.User;
import com.arthurscarpin.acs.core.user.exception.EmailUserAlreadyExistsException;
import com.arthurscarpin.acs.core.user.gateway.UserGateway;

import java.util.List;
import java.util.UUID;

public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserGateway userGateway;

    private final ScopeGateway scopeGateway;

    public RegisterUserUseCaseImpl(UserGateway userGateway, ScopeGateway scopeGateway) {
        this.userGateway = userGateway;
        this.scopeGateway = scopeGateway;
    }

    @Override
    public User execute(User user) {
        if (userGateway.existsByEmail(user.email())) {
            throw new EmailUserAlreadyExistsException("Email " + user.email() + " already exists");
        }

        List<UUID> existingScopes = scopeGateway.findAllIdsByIds(user.scopes());

        if (existingScopes.size() != user.scopes().size()) {
            throw new ScopeNotFoundException("Scope "  + user.scopes() + " does not exist");
        }

        return userGateway.save(user);
    }
}
