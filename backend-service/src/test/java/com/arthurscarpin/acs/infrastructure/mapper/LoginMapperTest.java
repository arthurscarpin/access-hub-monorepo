package com.arthurscarpin.acs.infrastructure.mapper;

import com.arthurscarpin.acs.core.user.domain.User;
import com.arthurscarpin.acs.infrastructure.presentation.request.LoginRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class LoginMapperTest {

    private final LoginMapper mapper = Mappers.getMapper(LoginMapper.class);

    @Test
    @DisplayName("Given LoginRequest When mapping to domain Then should map email and password")
    void shouldMapLoginRequestToUserDomain() {
        LoginRequest request = new LoginRequest(
                "contact@example.com",
                "Password@1234"
        );

        User result = mapper.fromRequestToDomain(request);

        assertNotNull(result);
        assertNull(result.id());
        assertEquals(request.email(), result.email());
        assertEquals(request.password(), result.password());
        assertNull(result.name());
        assertNull(result.active());
        assertNull(result.scopes());
    }

    @Test
    @DisplayName("Given null LoginRequest When mapping Then should return null")
    void shouldReturnNullWhenLoginRequestIsNull() {
        assertNull(mapper.fromRequestToDomain(null));
    }
}