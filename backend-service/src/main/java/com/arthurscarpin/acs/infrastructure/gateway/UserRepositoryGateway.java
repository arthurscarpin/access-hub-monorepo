package com.arthurscarpin.acs.infrastructure.gateway;

import com.arthurscarpin.acs.core.pagination.PageInput;
import com.arthurscarpin.acs.core.pagination.PageOutput;
import com.arthurscarpin.acs.core.user.domain.User;
import com.arthurscarpin.acs.core.user.gateway.LoginGateway;
import com.arthurscarpin.acs.core.user.gateway.UserGateway;
import com.arthurscarpin.acs.infrastructure.mapper.UserMapper;
import com.arthurscarpin.acs.infrastructure.persistence.entity.table.ScopeEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.table.UserEntity;
import com.arthurscarpin.acs.infrastructure.persistence.repository.table.ScopeRepository;
import com.arthurscarpin.acs.infrastructure.persistence.repository.table.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Override
    public PageOutput<User> findByFilters(PageInput pageInput) {
        Pageable pageable = PageRequest.of(
                pageInput.pageNumber(),
                pageInput.pageSize(),
                Sort.by(Sort.Direction.ASC, "name")
        );

        Page<UserEntity> page = userRepository.findAll(pageable);

        List<User> content = page.getContent().stream()
                .map(mapper::fromEntityToDomain)
                .toList();

        return new PageOutput<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
