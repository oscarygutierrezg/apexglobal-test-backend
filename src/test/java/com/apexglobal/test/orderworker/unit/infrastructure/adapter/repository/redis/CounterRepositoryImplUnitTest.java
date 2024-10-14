package com.apexglobal.test.orderworker.unit.infrastructure.adapter.repository.redis;

import com.apexglobal.test.orderworker.infrastructure.adapter.repository.redis.CounterRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CounterRepositoryImplUnitTest {

    @Mock
    private RedisTemplate<String, Integer> redisTemplateCounter;

    @Mock
    private ValueOperations<String, Integer> valueOperations;

    @InjectMocks
    private CounterRepositoryImpl counterRepository;

    @BeforeEach
    void setUp() {
        // Mock the ValueOperations to return the mock instance when accessed from RedisTemplate
        when(redisTemplateCounter.opsForValue()).thenReturn(valueOperations);
    }
    @Test
    void givenValidKey_whenIncrementCounter_thenReturnIncrementedValue() {
        // Given
        String givenKey = "order123";
        when(valueOperations.increment(anyString(), anyLong())).thenReturn(2L);  // Mocked return value

        // When
        Long incrementedValue = counterRepository.incrementCounter(givenKey);

        // Then
        assertEquals(2L, incrementedValue);
        verify(valueOperations).increment("counter_" + givenKey, 1L);  // Verifies the interaction
    }

    @Test
    void givenValidKey_whenDecrementCounter_thenReturnDecrementedValue() {
        // Given
        String givenKey = "order123";
        when(valueOperations.decrement(anyString(), anyLong())).thenReturn(0L);  // Mocked return value

        // When
        Long decrementedValue = counterRepository.decrementCounter(givenKey);

        // Then
        assertEquals(0L, decrementedValue);
        verify(valueOperations).decrement("counter_" + givenKey, 1L);  // Verifies the interaction
    }

    @Test
    void givenValidKey_whenGetCounterValue_thenReturnValue() {
        // Given
        String givenKey = "order123";
        when(valueOperations.get(anyString())).thenReturn(5);  // Mocked return value

        // When
        Long counterValue = counterRepository.getCounterValue(givenKey);

        // Then
        assertEquals(5L, counterValue);
        verify(valueOperations).get("counter_" + givenKey);  // Verifies the interaction
    }

    @Test
    void givenKeyNotPresent_whenGetCounterValue_thenReturnZero() {
        // Given
        String givenKey = "order123";
        when(valueOperations.get(anyString())).thenReturn(null);  // Mocked null return

        // When
        Long counterValue = counterRepository.getCounterValue(givenKey);

        // Then
        assertEquals(0L, counterValue);
        verify(valueOperations).get("counter_" + givenKey);  // Verifies the interaction
    }
}
