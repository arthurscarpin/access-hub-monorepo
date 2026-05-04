package com.arthurscarpin.acs.infrastructure.mapper;

import com.arthurscarpin.acs.core.scope.domain.Scope;
import com.arthurscarpin.acs.infrastructure.persistence.entity.ScopeEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ScopeMapperTest {

    private final ScopeMapper mapper = Mappers.getMapper(ScopeMapper.class);

    @Test
    @DisplayName("Given ScopeEntity When mapping to domain Then should return Scope")
    void shouldMapScopeEntityToDomain() {
        ScopeEntity entity = ScopeEntity.builder()
                .id(UUID.randomUUID())
                .name("ADMIN")
                .build();

        Scope result = mapper.fromEntityToDomain(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.id());
        assertEquals(entity.getName(), result.name());
    }

    @Test
    @DisplayName("Given null ScopeEntity When mapping Then should return null")
    void shouldReturnNullWhenScopeEntityIsNull() {
        assertNull(mapper.fromEntityToDomain(null));
    }
}