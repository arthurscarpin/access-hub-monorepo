package com.arthurscarpin.acs.core.user.usecase;

import com.arthurscarpin.acs.core.scope.domain.Scope;
import com.arthurscarpin.acs.core.scope.gateway.ScopeGateway;
import com.arthurscarpin.acs.core.user.domain.User;
import com.arthurscarpin.acs.core.user.exception.BadCredentialsException;
import com.arthurscarpin.acs.core.user.gateway.LoginGateway;
import com.arthurscarpin.acs.core.user.gateway.UserGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseImplTest {

    @InjectMocks
    private LoginUserUseCaseImpl useCase;

    @Mock
    private UserGateway userGateway;

    @Mock
    private LoginGateway loginGateway;

    @Mock
    private ScopeGateway scopeGateway;

    @Test
    @DisplayName("Given valid credentials, when logging in, then should return token and expiration time")
    void shouldLoginSuccessfully() {
        User userInput = new User(
                null,
                "Ana Santos",
                "contact@example.com",
                "Password@123",
                true,
                List.of(UUID.randomUUID(), UUID.randomUUID())
        );
        User userOutput = new User(
                UUID.randomUUID(),
                "Ana Santos",
                "contact@example.com",
                "Password@123",
                true,
                List.of(UUID.randomUUID(), UUID.randomUUID())
        );

        List<Scope> scopes = List.of(new Scope(UUID.randomUUID(), "admin:all"));

        when(userGateway.findByEmail(userInput.email())).thenReturn(Optional.of(userOutput));
        when(loginGateway.isPasswordCorrect(userInput.password(), userOutput.password())).thenReturn(true);
        when(scopeGateway.findAllByIdIn(userOutput.scopes())).thenReturn(scopes);
        when(loginGateway.generateToken(userOutput, 600L, scopes)).thenReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mock.sub.user.roles.signature");
        Object[] response = useCase.execute(userInput);

        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mock.sub.user.roles.signature", response[0]);
        assertEquals(600L, response[1]);
    }

    @Test
    @DisplayName("Given non-existent user, when logging in, then should throw BadCredentialsException")
    void shouldThrowExceptionWhenUserNotFound() {
        User user = new User(
                null,
                "Ana Santos",
                "contact@example.com",
                "Password@123",
                true,
                List.of(UUID.randomUUID(), UUID.randomUUID())
        );

        when(userGateway.findByEmail(user.email())).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> useCase.execute(user));
        verify(loginGateway, never()).generateToken(any(), anyLong(), any());
    }

    @Test
    @DisplayName("Given incorrect password, when logging in, then should throw BadCredentialsException")
    void shouldThrowExceptionWhenPasswordIsIncorrect() {
        User userInput = new User(
                null,
                "Ana Santos",
                "contact@example.com",
                "Password@123",
                true,
                List.of(UUID.randomUUID(), UUID.randomUUID())
        );
        User userOutput = new User(
                UUID.randomUUID(),
                "Ana Santos",
                "contact@example.com",
                "Password@123",
                true,
                List.of(UUID.randomUUID(), UUID.randomUUID())
        );
        when(userGateway.findByEmail(userInput.email())).thenReturn(Optional.of(userOutput));
        when(loginGateway.isPasswordCorrect(userInput.password(), userOutput.password())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> useCase.execute(userInput));
        verify(loginGateway, never()).generateToken(any(), anyLong(), any());
    }

    @Test
    @DisplayName("Given successful login, when processing request, then should load user scopes from gateway")
    void shouldLoadScopesWhenLoginSucceeds() {
        User userInput = new User(
                null,
                "Ana Santos",
                "contact@example.com",
                "Password@123",
                true,
                List.of(UUID.randomUUID(), UUID.randomUUID())
        );
        User userOutput = new User(
                UUID.randomUUID(),
                "Ana Santos",
                "contact@example.com",
                "Password@123",
                true,
                List.of(UUID.randomUUID(), UUID.randomUUID())
        );
        List<Scope> scopes = List.of(new Scope(UUID.randomUUID(), "admin:all"));

        when(userGateway.findByEmail(userInput.email())).thenReturn(Optional.of(userOutput));
        when(loginGateway.isPasswordCorrect(any(), any())).thenReturn(true);
        when(scopeGateway.findAllByIdIn(userOutput.scopes())).thenReturn(scopes);
        when(loginGateway.generateToken(any(), anyLong(), any())).thenReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mock.sub.user.roles.signature");
        useCase.execute(userInput);

        verify(scopeGateway).findAllByIdIn(userOutput.scopes());
    }

    @Test
    @DisplayName("Given successful login, when generating token, then should use correct user, expiry and scopes")
    void shouldGenerateTokenWithCorrectData() {
        User userInput = new User(
                null,
                "Ana Santos",
                "contact@example.com",
                "Password@123",
                true,
                List.of(UUID.randomUUID(), UUID.randomUUID())
        );
        User userOutput = new User(
                UUID.randomUUID(),
                "Ana Santos",
                "contact@example.com",
                "Password@123",
                true,
                List.of(UUID.randomUUID(), UUID.randomUUID())
        );List<Scope> scopes = List.of(new Scope(UUID.randomUUID(), "admin:all"));

        when(userGateway.findByEmail(userInput.email())).thenReturn(Optional.of(userOutput));
        when(loginGateway.isPasswordCorrect(any(), any())).thenReturn(true);
        when(scopeGateway.findAllByIdIn(userOutput.scopes())).thenReturn(scopes);
        when(loginGateway.generateToken(userOutput, 600L, scopes)).thenReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mock.sub.user.roles.signature");
        useCase.execute(userInput);

        verify(loginGateway).generateToken(userOutput, 600L, scopes);
    }
}