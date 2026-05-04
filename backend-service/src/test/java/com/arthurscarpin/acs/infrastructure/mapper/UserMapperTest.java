package com.arthurscarpin.acs.infrastructure.mapper;

import com.arthurscarpin.acs.core.user.domain.User;
import com.arthurscarpin.acs.infrastructure.persistence.entity.ScopeEntity;
import com.arthurscarpin.acs.infrastructure.persistence.entity.UserEntity;
import com.arthurscarpin.acs.infrastructure.presentation.request.UserRequest;
import com.arthurscarpin.acs.infrastructure.presentation.response.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    @DisplayName("Given UserRequest When mapping to domain Then should return User")
    void shouldMapUserRequestToDomain() {
        List<UUID> scopes = List.of(
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        UserRequest request = new UserRequest(
                "Ana Santos",
                "contact@example.com",
                "Password@123",
                scopes
        );

        User result = mapper.fromRequestToDomain(request);

        assertNotNull(result);
        assertNull(result.id());
        assertEquals(request.name(), result.name());
        assertEquals(request.email(), result.email());
        assertEquals(request.password(), result.password());
        assertEquals(request.scopes(), result.scopes());
    }

    @Test
    @DisplayName("Given User domain When mapping to response Then should return UserResponse")
    void shouldMapUserDomainToResponse() {
        List<UUID> scopes = List.of(UUID.randomUUID(), UUID.randomUUID());

        User domain = new User(
                UUID.randomUUID(),
                "Ana Santos",
                "contact@example.com",
                "Password@123",
                true,
                scopes
        );

        UserResponse response = mapper.fromDomainToResponse(domain);

        assertNotNull(response);
        assertEquals(domain.id(), response.id());
        assertEquals(domain.name(), response.name());
        assertEquals(domain.email(), response.email());
        assertIterableEquals(
                domain.scopes().stream().map(UUID::toString).toList(),
                response.scopes()
        );
    }

    @Test
    @DisplayName("Given User domain When mapping to entity Then should return UserEntity")
    void shouldMapUserDomainToEntity() {
        User domain = new User(
                UUID.randomUUID(),
                "Ana Santos",
                "contact@example.com",
                "Password@123",
                true,
                List.of(UUID.randomUUID())
        );

        UserEntity entity = mapper.fromDomainToEntity(domain);

        assertNotNull(entity);
        assertEquals(domain.id(), entity.getId());
        assertEquals(domain.name(), entity.getName());
        assertEquals(domain.email(), entity.getEmail());
        assertEquals(domain.password(), entity.getPassword());
        assertEquals(domain.active(), entity.getActive());
        assertNotNull(entity.getScopes());
    }

    @Test
    @DisplayName("Given UserEntity When mapping to domain Then should return User with scope IDs")
    void shouldMapUserEntityToDomain() {
        ScopeEntity scope1 = ScopeEntity.builder()
                .id(UUID.randomUUID())
                .name("ADMIN")
                .build();

        ScopeEntity scope2 = ScopeEntity.builder()
                .id(UUID.randomUUID())
                .name("USER")
                .build();

        UserEntity entity = UserEntity.builder()
                .id(UUID.randomUUID())
                .name("Ana Santos")
                .email("contact@example.com")
                .password("Password@123")
                .active(true)
                .scopes(List.of(scope1, scope2))
                .build();

        User result = mapper.fromEntityToDomain(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.id());
        assertEquals(entity.getName(), result.name());
        assertEquals(entity.getEmail(), result.email());
        assertEquals(entity.getPassword(), result.password());
        assertEquals(entity.getActive(), result.active());

        assertIterableEquals(
                List.of(scope1.getId(), scope2.getId()),
                result.scopes()
        );
    }

    @Test
    @DisplayName("Given null values When mapping Then should return null safely")
    void shouldReturnNullWhenMappingNullValues() {
        assertNull(mapper.fromRequestToDomain(null));
        assertNull(mapper.fromDomainToResponse(null));
        assertNull(mapper.fromDomainToEntity(null));
        assertNull(mapper.fromEntityToDomain(null));
    }
}