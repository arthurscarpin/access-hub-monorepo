package com.arthurscarpin.acs.infrastructure.mapper;

import com.arthurscarpin.acs.core.owner.domain.DocumentType;
import com.arthurscarpin.acs.core.owner.domain.Owner;
import com.arthurscarpin.acs.infrastructure.persistence.entity.OwnerEntity;
import com.arthurscarpin.acs.infrastructure.presentation.request.OwnerRequest;
import com.arthurscarpin.acs.infrastructure.presentation.response.OwnerResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OwnerMapperTest {

    private final OwnerMapper mapper = Mappers.getMapper(OwnerMapper.class);

    @Test
    @DisplayName("Given OwnerRequest When mapping to domain Then should return Owner")
    void shouldMapOwnerRequestToDomain() {
        OwnerRequest request = new OwnerRequest(
                "Maria Oliveira",
                "111.444.777-35",
                DocumentType.CPF,
                "contact@example.com"
        );

        Owner result = mapper.fromRequestToDomain(request);

        assertNotNull(result);
        assertNull(result.id());
        assertEquals(request.name(), result.name());
        assertEquals(request.document(), result.document());
        assertEquals(request.documentType(), result.documentType());
        assertEquals(request.email(), result.email());
    }

    @Test
    @DisplayName("Given Owner domain When mapping to response Then should return OwnerResponse")
    void shouldMapOwnerDomainToResponse() {
        Owner domain = new Owner(
                UUID.randomUUID(),
                "Maria Oliveira",
                "111.444.777-35",
                DocumentType.CPF,
                "contact@example.com"
        );

        OwnerResponse response = mapper.fromDomainToResponse(domain);

        assertNotNull(response);
        assertEquals(domain.id(), response.id());
        assertEquals(domain.name(), response.name());
        assertEquals(domain.document(), response.document());
        assertEquals(domain.documentType(), response.documentType());
        assertEquals(domain.email(), response.email());
    }

    @Test
    @DisplayName("Given Owner domain When mapping to entity Then should return OwnerEntity")
    void shouldMapOwnerDomainToEntity() {
        Owner domain = new Owner(
                UUID.randomUUID(),
                "Maria Oliveira",
                "111.444.777-35",
                DocumentType.CPF,
                "contact@example.com"
        );

        OwnerEntity entity = mapper.fromDomainToEntity(domain);

        assertNotNull(entity);
        assertEquals(domain.id(), entity.getId());
        assertEquals(domain.name(), entity.getName());
        assertEquals(domain.document(), entity.getDocument());
        assertEquals(domain.documentType(), entity.getDocumentType());
        assertEquals(domain.email(), entity.getEmail());
    }

    @Test
    @DisplayName("Given OwnerEntity When mapping to domain Then should return Owner")
    void shouldMapOwnerEntityToDomain() {
        OwnerEntity entity = OwnerEntity.builder()
                .id(UUID.randomUUID())
                .name("Maria Oliveira")
                .document("111.444.777-35")
                .documentType(DocumentType.CPF)
                .email("contact@example.com")
                .build();

        Owner result = mapper.fromEntityToDomain(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.id());
        assertEquals(entity.getName(), result.name());
        assertEquals(entity.getDocument(), result.document());
        assertEquals(entity.getDocumentType(), result.documentType());
        assertEquals(entity.getEmail(), result.email());
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