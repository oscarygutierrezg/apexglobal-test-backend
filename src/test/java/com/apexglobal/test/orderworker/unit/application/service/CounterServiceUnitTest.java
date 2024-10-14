package com.apexglobal.test.orderworker.unit.application.service;

import com.apexglobal.test.orderworker.application.service.CounterService;
import com.apexglobal.test.orderworker.domain.port.outgoing.CounterRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CounterServiceUnitTest {

    @Mock
    private CounterRepositoryPort counterRepository;

    @InjectMocks
    private CounterService counterService;

    @Test
    void givenValidgivenKey_whenIncrementCounter_thenReturnIncrementedValue() {
        // Given
        String givenKey = "order123";
        when(counterRepository.incrementCounter(givenKey)).thenReturn(2L); // Mocked return value

        // When
        Long incrementedValue = counterService.incrementCounter(givenKey);

        // Then
        assertEquals(2L, incrementedValue);
        verify(counterRepository).incrementCounter(givenKey); // Verifies the interaction
    }

    @Test
    void givenValidgivenKey_whenDecrementCounter_thenReturnDecrementedValue() {
        // Given
        String givenKey = "order123";
        when(counterRepository.decrementCounter(givenKey)).thenReturn(0L); // Mocked return value

        // When
        Long decrementedValue = counterService.decrementCounter(givenKey);

        // Then
        assertEquals(0L, decrementedValue);
        verify(counterRepository).decrementCounter(givenKey); // Verifies the interaction
    }

    @Test
    void givenValidgivenKey_whenGetCounterValue_thenReturnValue() {
        // Given
        String givenKey = "order123";
        when(counterRepository.getCounterValue(givenKey)).thenReturn(5L); // Mocked return value

        // When
        Long counterValue = counterService.getCounterValue(givenKey);

        // Then
        assertEquals(5L, counterValue);
        verify(counterRepository).getCounterValue(givenKey); // Verifies the interaction
    }

    @Test
    void givengivenKeyNotPresent_whenGetCounterValue_thenReturnZero() {
        // Given
        String givenKey = "order123";
        when(counterRepository.getCounterValue(givenKey)).thenReturn(0L); // Mocked return value for non-existent givenKey

        // When
        Long counterValue = counterService.getCounterValue(givenKey);

        // Then
        assertEquals(0L, counterValue);
        verify(counterRepository).getCounterValue(givenKey); // Verifies the interaction
    }
}
