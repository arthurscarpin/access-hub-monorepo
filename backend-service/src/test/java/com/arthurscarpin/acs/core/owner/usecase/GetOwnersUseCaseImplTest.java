package com.arthurscarpin.acs.core.owner.usecase;

import com.arthurscarpin.acs.core.owner.domain.DocumentType;
import com.arthurscarpin.acs.core.owner.domain.Owner;
import com.arthurscarpin.acs.core.owner.gateway.OwnerGateway;
import com.arthurscarpin.acs.core.pagination.PageInput;
import com.arthurscarpin.acs.core.pagination.PageOutput;
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
class GetOwnersUseCaseImplTest {

    @InjectMocks
    private GetOwnersUseCaseImpl useCase;

    @Mock
    private OwnerGateway gateway;

    @Test
    @DisplayName("Given valid pagination, when listing owners, then should return paginated result successfully")
    void shouldReturnPaginatedOwnersSuccessfully() {

        PageInput input = new PageInput(
                0,
                10,
                null,
                null,
                null
        );

        List<Owner> owners = List.of(
                new Owner(
                        UUID.randomUUID(),
                        "Maria Oliveira",
                        "11144477735",
                        DocumentType.CPF,
                        "maria@example.com"
                ),
                new Owner(
                        UUID.randomUUID(),
                        "João Silva",
                        "98765432100",
                        DocumentType.CPF,
                        "joao@example.com"
                )
        );

        PageOutput<Owner> expectedOutput = new PageOutput<>(
                owners,
                0,
                10,
                2,
                1
        );

        when(gateway.findByFilters(input)).thenReturn(expectedOutput);

        PageOutput<Owner> response = useCase.execute(input);

        assertNotNull(response);
        assertEquals(2, response.content().size());
        assertEquals(2, response.totalElements());
        assertEquals(1, response.totalPages());
        assertEquals(0, response.page());
        assertEquals(10, response.size());

        verify(gateway).findByFilters(input);
    }

    @Test
    @DisplayName("Given pagination without results, when listing owners, then should return empty page")
    void shouldReturnEmptyPageWhenNoOwnersFound() {

        PageInput input = new PageInput(
                0,
                10,
                null,
                null,
                null
        );

        PageOutput<Owner> expectedOutput = new PageOutput<>(
                List.of(),
                0,
                10,
                0,
                0
        );

        when(gateway.findByFilters(input)).thenReturn(expectedOutput);

        PageOutput<Owner> response = useCase.execute(input);

        assertNotNull(response);
        assertTrue(response.content().isEmpty());
        assertEquals(0, response.totalElements());
        assertEquals(0, response.totalPages());

        verify(gateway).findByFilters(input);
    }

    @Test
    @DisplayName("Given valid page input, when executing use case, then should call gateway once")
    void shouldCallGatewayOnce() {

        PageInput input = new PageInput(
                0,
                5,
                null,
                null,
                null
        );

        PageOutput<Owner> output = new PageOutput<>(
                List.of(),
                0,
                5,
                0,
                0
        );

        when(gateway.findByFilters(input)).thenReturn(output);

        useCase.execute(input);

        verify(gateway, times(1)).findByFilters(input);
    }

    @Test
    @DisplayName("Given valid pagination, when executing use case, then should return same gateway response")
    void shouldReturnSameGatewayResponse() {

        PageInput input = new PageInput(
                0,
                5,
                null,
                null,
                null
        );

        PageOutput<Owner> expectedOutput = new PageOutput<>(
                List.of(
                        new Owner(
                                UUID.randomUUID(),
                                "Carlos Souza",
                                "12345678900",
                                DocumentType.CPF,
                                "carlos@example.com"
                        )
                ),
                0,
                5,
                1,
                1
        );

        when(gateway.findByFilters(input)).thenReturn(expectedOutput);

        PageOutput<Owner> response = useCase.execute(input);

        assertSame(expectedOutput, response);

        verify(gateway).findByFilters(input);
    }

    @Test
    @DisplayName("Given gateway failure, when listing owners, then should propagate exception")
    void shouldPropagateExceptionWhenGatewayFails() {

        PageInput input = new PageInput(
                0,
                10,
                null,
                null,
                null
        );

        when(gateway.findByFilters(input))
                .thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> useCase.execute(input)
        );

        assertEquals("Database error", exception.getMessage());

        verify(gateway).findByFilters(input);
    }
}