package com.arthurscarpin.acs.core.user.gateway;

import com.arthurscarpin.acs.core.user.domain.User;

import java.util.Optional;

public interface UserGateway {

    boolean existsByEmail(String email);

    User save(User user);

    Optional<User> findByEmail(String email);
}
