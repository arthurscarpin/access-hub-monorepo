package com.arthurscarpin.acs.core.user.usecase;

import com.arthurscarpin.acs.core.pagination.PageInput;
import com.arthurscarpin.acs.core.pagination.PageOutput;
import com.arthurscarpin.acs.core.user.domain.User;
import com.arthurscarpin.acs.core.user.gateway.UserGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUsersUseCaseImplTest {

    @InjectMocks
    private GetUsersUseCaseImpl useCase;

    @Mock
    private UserGateway gateway;

    @Test
    @DisplayName("Given valid pagination, when listing users, then should return paginated users successfully")
    void shouldReturnPaginatedUsersSuccessfully() {
        PageInput pageInput = new PageInput(
                0,
                10,
                null,
                null,
                null
        );

        List<User> users = List.of(
                new User(
                        UUID.randomUUID(),
                        "Ana Santos",
                        "ana@example.com",
                        "Password@123",
                        true,
                        List.of(UUID.randomUUID())
                ),
                new User(
                        UUID.randomUUID(),
                        "Carlos Silva",
                        "carlos@example.com",
                        "Password@123",
                        true,
                        List.of(UUID.randomUUID())
                )
        );

        PageOutput<User> expectedResponse = new PageOutput<>(
                users,
                0,
                10,
                2,
                1
        );

        when(gateway.findByFilters(pageInput)).thenReturn(expectedResponse);

        PageOutput<User> response = useCase.execute(pageInput);

        assertNotNull(response);
        assertEquals(2, response.totalElements());
        assertEquals(1, response.totalPages());
        assertEquals(2, response.content().size());
        assertEquals(users, response.content());

        verify(gateway).findByFilters(pageInput);
    }

    @Test
    @DisplayName("Given no users found, when listing users, then should return empty page")
    void shouldReturnEmptyPageWhenNoUsersFound() {
        PageInput pageInput = new PageInput(
                0,
                10,
                null,
                null,
                null
        );

        PageOutput<User> expectedResponse = new PageOutput<>(
                List.of(),
                0,
                10,
                0,
                0
        );

        when(gateway.findByFilters(pageInput)).thenReturn(expectedResponse);

        PageOutput<User> response = useCase.execute(pageInput);

        assertNotNull(response);
        assertTrue(response.content().isEmpty());
        assertEquals(0, response.totalElements());
        assertEquals(0, response.totalPages());

        verify(gateway).findByFilters(pageInput);
    }

    @Test
    @DisplayName("Given filters, when listing users, then should forward filters correctly to gateway")
    void shouldForwardFiltersCorrectlyToGateway() {
        Instant from = Instant.parse("2026-01-01T00:00:00Z");
        Instant to = Instant.parse("2026-12-31T23:59:59Z");

        PageInput pageInput = new PageInput(
                0,
                10,
                "ABC1234",
                from,
                to
        );

        PageOutput<User> expectedResponse = new PageOutput<>(
                List.of(),
                0,
                10,
                0,
                0
        );

        when(gateway.findByFilters(pageInput)).thenReturn(expectedResponse);

        useCase.execute(pageInput);

        verify(gateway).findByFilters(pageInput);
    }

    @Test
    @DisplayName("Given gateway error, when listing users, then should propagate exception")
    void shouldPropagateExceptionWhenGatewayFails() {
        PageInput pageInput = new PageInput(
                0,
                10,
                null,
                null,
                null
        );

        RuntimeException exception = new RuntimeException("Gateway error");

        when(gateway.findByFilters(pageInput)).thenThrow(exception);

        RuntimeException response = assertThrows(
                RuntimeException.class,
                () -> useCase.execute(pageInput)
        );

        assertEquals("Gateway error", response.getMessage());

        verify(gateway).findByFilters(pageInput);
    }

    @Test
    @DisplayName("Given null page input, when listing users, then should propagate exception")
    void shouldThrowExceptionWhenPageInputIsNull() {
        when(gateway.findByFilters(null))
                .thenThrow(new NullPointerException("pageInput cannot be null"));

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> useCase.execute(null)
        );

        assertEquals("pageInput cannot be null", exception.getMessage());

        verify(gateway).findByFilters(null);
    }

    @Test
    @DisplayName("Given large page size, when listing users, then should return users successfully")
    void shouldReturnUsersSuccessfullyWithLargePageSize() {
        PageInput pageInput = new PageInput(
                0,
                100,
                null,
                null,
                null
        );

        List<User> users = List.of(
                new User(
                        UUID.randomUUID(),
                        "Ana Santos",
                        "ana@example.com",
                        "Password@123",
                        true,
                        List.of(UUID.randomUUID())
                )
        );

        PageOutput<User> expectedResponse = new PageOutput<>(
                users,
                0,
                100,
                1,
                1
        );

        when(gateway.findByFilters(pageInput)).thenReturn(expectedResponse);

        PageOutput<User> response = useCase.execute(pageInput);

        assertNotNull(response);
        assertEquals(1, response.totalElements());
        assertEquals(1, response.totalPages());
        assertEquals(1, response.content().size());

        verify(gateway).findByFilters(pageInput);
    }

    @Test
    @DisplayName("Given date filters, when listing users, then should return filtered users successfully")
    void shouldReturnFilteredUsersByDateSuccessfully() {
        Instant from = Instant.parse("2026-01-01T00:00:00Z");
        Instant to = Instant.parse("2026-12-31T23:59:59Z");

        PageInput pageInput = new PageInput(
                0,
                10,
                null,
                from,
                to
        );

        List<User> users = List.of(
                new User(
                        UUID.randomUUID(),
                        "Maria Oliveira",
                        "maria@example.com",
                        "Password@123",
                        true,
                        List.of(UUID.randomUUID())
                )
        );

        PageOutput<User> expectedResponse = new PageOutput<>(
                users,
                0,
                10,
                1,
                1
        );

        when(gateway.findByFilters(pageInput)).thenReturn(expectedResponse);

        PageOutput<User> response = useCase.execute(pageInput);

        assertNotNull(response);
        assertEquals(1, response.content().size());
        assertEquals(users, response.content());

        verify(gateway).findByFilters(pageInput);
    }
}