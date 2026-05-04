package com.arthurscarpin.acs.infrastructure.gateway;

import com.arthurscarpin.acs.core.user.domain.User;
import com.arthurscarpin.acs.core.user.gateway.LoginGateway;
import com.arthurscarpin.acs.core.user.gateway.UserGateway;
import com.arthurscarpin.acs.infrastructure.mapper.UserMapper;
import com.arthurscarpin.acs.infrastructure.persistence.entity.ScopeEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.UserEntity;
import com.arthurscarpin.acs.infrastructure.persistence.repository.ScopeRepository;
import com.arthurscarpin.acs.infrastructure.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryGateway implements UserGateway {

    private final UserRepository userRepository;

    private final ScopeRepository scopeRepository;

    private final UserMapper mapper;

    private final LoginGateway loginGateway;

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    @Override
    public User save(User user) {
        List<ScopeEntity> scopes = user.scopes().stream()
                .map(scopeRepository::getReferenceById)
                .toList();
        UserEntity savedUser = mapper.fromDomainToEntity(user);
        savedUser.setScopes(scopes);
        savedUser.setPassword(loginGateway.encryptPassword(user.password()));
        savedUser.setActive(true);
        return mapper.fromEntityToDomain(userRepository.save(savedUser));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailWithScopes(email)
                .map(mapper::fromEntityToDomain);
    }
}
