package com.arthurscarpin.acs.infrastructure.mapper;

import com.arthurscarpin.acs.core.accessevent.domain.AccessEvent;
import com.arthurscarpin.acs.core.accessevent.domain.AccessResult;
import com.arthurscarpin.acs.core.accessevent.domain.Direction;
import com.arthurscarpin.acs.infrastructure.persistence.entity.AccessEventEntity;
import com.arthurscarpin.acs.infrastructure.presentation.request.AccessEventRequest;
import com.arthurscarpin.acs.infrastructure.presentation.response.AccessEventResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AccessEventMapper Tests")
class AccessEventMapperTest {

    private final AccessEventMapper mapper = Mappers.getMapper(AccessEventMapper.class);

    @Test
    @DisplayName("Given AccessEventRequest When mapping to Domain Then returns AccessEvent correctly")
    void givenRequest_whenMapToDomain_thenReturnsDomain() {
        AccessEventRequest request = new AccessEventRequest(
                "BRA1S23",
                Direction.IN,
                OffsetDateTime.parse("2026-01-01T00:00:00Z")
        );

        AccessEvent result = mapper.fromRequestToDomain(request);

        assertNotNull(result);
        assertEquals("BRA1S23", result.plate());
        assertEquals(Direction.IN, result.direction());
        assertEquals(request.timestamp(), result.timestamp());
        assertNull(result.id());
    }

    @Test
    @DisplayName("Given AccessEvent Domain When mapping to Response Then returns AccessEventResponse correctly")
    void givenDomain_whenMapToResponse_thenReturnsResponse() {
        AccessEvent domain = new AccessEvent(
                UUID.randomUUID(),
                "BRA1S23",
                OffsetDateTime.parse("2026-01-01T00:00:00Z"),
                Direction.OUT,
                AccessResult.AUTHORIZED
        );

        AccessEventResponse response = mapper.fromDomainToResponse(domain);

        assertNotNull(response);
        assertEquals(domain.id(), response.id());
        assertEquals(domain.plate(), response.plate());
        assertEquals(domain.timestamp(), response.timestamp());
        assertEquals(domain.direction(), response.direction());
        assertEquals(domain.result(), response.result());
    }

    @Test
    @DisplayName("Given AccessEvent Domain When mapping to Entity Then returns AccessEventEntity correctly")
    void givenDomain_whenMapToEntity_thenReturnsEntity() {
        AccessEvent domain = new AccessEvent(
                UUID.randomUUID(),
                "BRA1S23",
                OffsetDateTime.parse("2026-01-01T00:00:00Z"),
                Direction.IN,
                AccessResult.DENIED
        );

        AccessEventEntity entity = mapper.fromDomainToEntity(domain);

        assertNotNull(entity);
        assertEquals(domain.id(), entity.getId());
        assertEquals(domain.plate(), entity.getPlate());
        assertEquals(domain.timestamp(), entity.getTimestamp());
        assertEquals(domain.direction(), entity.getDirection());
        assertEquals(domain.result(), entity.getResult());
    }

    @Test
    @DisplayName("Given AccessEventEntity When mapping to Domain Then returns AccessEvent correctly")
    void givenEntity_whenMapToDomain_thenReturnsDomain() {
        AccessEventEntity entity = AccessEventEntity.builder()
                .id(UUID.randomUUID())
                .plate("BRA1S23")
                .timestamp(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .direction(Direction.OUT)
                .result(AccessResult.AUTHORIZED)
                .build();

        AccessEvent domain = mapper.fromEntityToDomain(entity);

        assertNotNull(domain);
        assertEquals(entity.getId(), domain.id());
        assertEquals(entity.getPlate(), domain.plate());
        assertEquals(entity.getTimestamp(), domain.timestamp());
        assertEquals(entity.getDirection(), domain.direction());
        assertEquals(entity.getResult(), domain.result());
    }

    @Test
    @DisplayName("Given null values When mapping Then returns null safely")
    void givenNullValues_whenMapping_thenReturnsNull() {
        assertNull(mapper.fromRequestToDomain(null));
        assertNull(mapper.fromDomainToResponse(null));
        assertNull(mapper.fromDomainToEntity(null));
        assertNull(mapper.fromEntityToDomain(null));
    }
}