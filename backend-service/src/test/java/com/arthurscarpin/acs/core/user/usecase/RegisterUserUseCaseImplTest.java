package com.arthurscarpin.acs.core.user.usecase;

import com.arthurscarpin.acs.core.scope.exception.ScopeNotFoundException;
import com.arthurscarpin.acs.core.scope.gateway.ScopeGateway;
import com.arthurscarpin.acs.core.user.domain.User;
import com.arthurscarpin.acs.core.user.exception.EmailUserAlreadyExistsException;
import com.arthurscarpin.acs.core.user.gateway.UserGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseImplTest {

    @InjectMocks
    private RegisterUserUseCaseImpl useCase;

    @Mock
    private UserGateway userGateway;

    @Mock
    private ScopeGateway scopeGateway;

    @Test
    @DisplayName("Given valid user data, when registering, then should save user successfully")
    void shouldRegisterUserSuccessfully() {
        User user = new User(
                null,
                "Ana Santos",
                "contact@example.com",
                "Password@123",
                true,
                List.of(UUID.randomUUID(), UUID.randomUUID())
        );

        when(userGateway.existsByEmail(user.email())).thenReturn(false);
        when(scopeGateway.findAllIdsByIds(user.scopes())).thenReturn(user.scopes());
        when(userGateway.save(any())).thenAnswer(inv -> inv.getArgument(0));
        User response = useCase.execute(user);

        assertEquals(user.email(), response.email());
        verify(userGateway).save(user);
    }

    @Test
    @DisplayName("Given existing email, when registering user, then should throw EmailUserAlreadyExistsException")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        User user = new User(
                null,
                "Ana Santos",
                "contact@example.com",
                "Password@123",
                true,
                List.of(UUID.randomUUID(), UUID.randomUUID())
        );

        when(userGateway.existsByEmail(user.email())).thenReturn(true);

        assertThrows(EmailUserAlreadyExistsException.class, () -> useCase.execute(user));
        verify(userGateway, never()).save(any());
        verify(scopeGateway, never()).findAllIdsByIds(any());
    }

    @Test
    @DisplayName("Given non-existent scopes, when registering user, then should throw ScopeNotFoundException")
    void shouldThrowExceptionWhenScopeNotFound() {
        User user = new User(
                null,
                "Ana Santos",
                "contact@example.com",
                "Password@123",
                true,
                List.of(UUID.randomUUID(), UUID.randomUUID())
        );

        when(userGateway.existsByEmail(user.email())).thenReturn(false);
        when(scopeGateway.findAllIdsByIds(user.scopes())).thenReturn(List.of());

        assertThrows(ScopeNotFoundException.class, () -> useCase.execute(user));
        verify(userGateway, never()).save(any());
    }

    @Test
    @DisplayName("Given incomplete scopes, when registering user, then should throw ScopeNotFoundException")
    void shouldThrowExceptionWhenScopesAreIncomplete() {
        User user = new User(
                null,
                "Ana Santos",
                "contact@example.com",
                "Password@123",
                true,
                List.of(UUID.randomUUID(), UUID.randomUUID())
        );
        List<UUID> partialScopes = List.of(user.scopes().get(0));

        when(userGateway.existsByEmail(user.email())).thenReturn(false);
        when(scopeGateway.findAllIdsByIds(user.scopes())).thenReturn(partialScopes);

        assertThrows(ScopeNotFoundException.class, () -> useCase.execute(user));
    }

    @Test
    @DisplayName("Given invalid user data, when registering, then should not persist user")
    void shouldNotSaveUserWhenValidationFails() {
        User user = new User(
                null,
                "Ana Santos",
                "contact@example.com",
                "Password@123",
                true,
                List.of(UUID.randomUUID(), UUID.randomUUID())
        );

        when(userGateway.existsByEmail(user.email())).thenReturn(true);

        assertThrows(EmailUserAlreadyExistsException.class, () -> useCase.execute(user));
        verify(userGateway, never()).save(any());
        verify(scopeGateway, never()).findAllIdsByIds(any());
    }
}