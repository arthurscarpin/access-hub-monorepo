package com.arthurscarpin.acs.core.accessevent.usecase;

import com.arthurscarpin.acs.core.accessevent.domain.AccessEvent;
import com.arthurscarpin.acs.core.accessevent.gateway.AccessEventGateway;
import com.arthurscarpin.acs.core.pagination.PageInput;
import com.arthurscarpin.acs.core.pagination.PageOutput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAccessHistoryUseCaseImplTest {

    @InjectMocks
    private GetAccessHistoryUseCaseImpl useCase;

    @Mock
    private AccessEventGateway gateway;

    @Test
    @DisplayName("Given valid pagination input, when executing use case, then should return paginated access events")
    void shouldReturnPageFromGateway() {
        PageInput pageInput = new PageInput(0, 10, null, null, null);
        PageOutput<AccessEvent> pageOutput = new PageOutput<>(List.of(), 0, 10, 0L, 0);

        when(gateway.findByFilters(pageInput)).thenReturn(pageOutput);
        PageOutput<AccessEvent> response = useCase.execute(pageInput);

        assertEquals(pageOutput, response);
        verify(gateway, times(1)).findByFilters(pageInput);
    }

    @Test
    @DisplayName("Given valid pagination input, when executing use case, then should return empty page when no results exist")
    void shouldReturnEmptyPage() {
        PageInput pageInput = new PageInput(0, 10, null, null, null);
        PageOutput<AccessEvent> pageOutput = new PageOutput<>(List.of(), 0, 10, 0L, 0);

        when(gateway.findByFilters(pageInput)).thenReturn(pageOutput);
        PageOutput<AccessEvent> response = useCase.execute(pageInput);

        assertNotNull(response);
        assertTrue(response.content().isEmpty());
        verify(gateway).findByFilters(pageInput);
    }

    @Test
    @DisplayName("Given gateway failure, when executing use case, then should propagate exception")
    void shouldPropagateException() {
        PageInput pageInput = new PageInput(0, 10, null, null, null);

        when(gateway.findByFilters(pageInput)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> useCase.execute(pageInput));
        verify(gateway).findByFilters(pageInput);
    }

    @Test
    @DisplayName("Given valid input, when executing use case, then should call gateway once with correct parameters")
    void shouldCallGatewayOnceWithCorrectInput() {
        PageInput pageInput = new PageInput(0, 10, null, null, null);
        PageOutput<AccessEvent> pageOutput = new PageOutput<>(List.of(), 0, 10, 0L, 0);

        when(gateway.findByFilters(pageInput)).thenReturn(pageOutput);
        useCase.execute(pageInput);

        verify(gateway, times(1)).findByFilters(pageInput);
        verifyNoMoreInteractions(gateway);
    }
}