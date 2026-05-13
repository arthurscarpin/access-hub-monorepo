package com.arthurscarpin.acs.core.user.usecase;

import com.arthurscarpin.acs.core.scope.domain.Scope;
import com.arthurscarpin.acs.core.scope.gateway.ScopeGateway;
import com.arthurscarpin.acs.core.user.domain.User;
import com.arthurscarpin.acs.core.user.exception.BadCredentialsException;
import com.arthurscarpin.acs.core.user.gateway.LoginGateway;
import com.arthurscarpin.acs.core.user.gateway.UserGateway;

import java.util.List;
import java.util.Optional;

public class LoginUserUseCaseImpl implements LoginUserUseCase {

    private final UserGateway userGateway;

    private final LoginGateway loginGateway;

    private final ScopeGateway scopeGateway;

    public LoginUserUseCaseImpl(UserGateway userGateway, LoginGateway loginGateway, ScopeGateway scopeGateway) {
        this.userGateway = userGateway;
        this.loginGateway = loginGateway;
        this.scopeGateway = scopeGateway;
    }

    @Override
    public Object[] execute(User user) {
        Optional<User> existsUser = userGateway.findByEmail(user.email());
        if (existsUser.isEmpty() || !loginGateway.isPasswordCorrect(user.password(), existsUser.get().password())) {
            throw new BadCredentialsException("The username or password is incorrect");
        }
        User savedUser = existsUser.get();
        List<Scope> scopes = scopeGateway.findAllByIdIn(savedUser.scopes());
        Long expiresIn = 600L;
        String token = loginGateway.generateToken(savedUser, expiresIn, scopes);
        return new Object[]{token, expiresIn};
    }
}
