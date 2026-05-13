package com.arthurscarpin.acs.core.user.gateway;

import com.arthurscarpin.acs.core.scope.domain.Scope;
import com.arthurscarpin.acs.core.user.domain.User;

import java.util.List;

public interface LoginGateway {

    boolean isPasswordCorrect(String password, String savedPassword);

    String generateToken(User savedUser, Long expiresIn, List<Scope> scopes);

    String encryptPassword(String password);
}
