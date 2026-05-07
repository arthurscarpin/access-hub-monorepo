package com.arthurscarpin.acs.core.scope.usecase;

import com.arthurscarpin.acs.core.vehicle.scope.domain.Scope;
import com.arthurscarpin.acs.core.vehicle.scope.gateway.ScopeGateway;
import com.arthurscarpin.acs.core.vehicle.scope.usecase.GetScopesUseCaseImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetScopesUseCaseImplTest {

    @InjectMocks
    private GetScopesUseCaseImpl useCase;

    @Mock
    private ScopeGateway gateway;

    @Test
    @DisplayName("Given available scopes, when executing use case, then should return all scopes")
    void shouldReturnAllScopes() {
        List<Scope> scopes = List.of(
                new Scope(UUID.randomUUID(), "admin:read"),
                new Scope(UUID.randomUUID(), "admin:write"),
                new Scope(UUID.randomUUID(), "user:read")
        );

        when(gateway.findAll()).thenReturn(scopes);
        List<Scope> response = useCase.execute();

        assertEquals(scopes, response);
        assertEquals(3, response.size());
        verify(gateway, times(1)).findAll();
    }

    @Test
    @DisplayName("Given no scopes exist, when executing use case, then should return empty list")
    void shouldReturnEmptyListWhenNoScopesExist() {
        List<Scope> scopes = List.of();

        when(gateway.findAll()).thenReturn(scopes);
        List<Scope> response = useCase.execute();

        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(gateway, times(1)).findAll();
    }

    @Test
    @DisplayName("Given gateway returns scopes with correct data, when executing use case, then should have correct scope details")
    void shouldReturnScopesWithCorrectDetails() {
        UUID adminId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        List<Scope> scopes = List.of(
                new Scope(adminId, "admin:all"),
                new Scope(userId, "user:read")
        );

        when(gateway.findAll()).thenReturn(scopes);
        List<Scope> response = useCase.execute();

        assertEquals("admin:all", response.get(0).name());
        assertEquals(adminId, response.get(0).id());
        assertEquals("user:read", response.get(1).name());
        assertEquals(userId, response.get(1).id());
    }

    @Test
    @DisplayName("Given gateway failure, when executing use case, then should propagate exception")
    void shouldPropagateExceptionWhenGatewayFails() {
        when(gateway.findAll()).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> useCase.execute());
        verify(gateway, times(1)).findAll();
    }

    @Test
    @DisplayName("Given valid execution, when executing use case, then should call gateway only once")
    void shouldCallGatewayOnlyOnce() {
        List<Scope> scopes = List.of(new Scope(UUID.randomUUID(), "admin:all"));

        when(gateway.findAll()).thenReturn(scopes);
        useCase.execute();

        verify(gateway, times(1)).findAll();
        verifyNoMoreInteractions(gateway);
    }

    @Test
    @DisplayName("Given multiple scopes, when executing use case, then should maintain order")
    void shouldMaintainOrderOfScopes() {
        UUID firstId = UUID.randomUUID();
        UUID secondId = UUID.randomUUID();
        UUID thirdId = UUID.randomUUID();
        List<Scope> scopes = List.of(
                new Scope(firstId, "scope1"),
                new Scope(secondId, "scope2"),
                new Scope(thirdId, "scope3")
        );

        when(gateway.findAll()).thenReturn(scopes);
        List<Scope> response = useCase.execute();

        assertEquals(firstId, response.get(0).id());
        assertEquals(secondId, response.get(1).id());
        assertEquals(thirdId, response.get(2).id());
    }
}