package com.apexglobal.test.orderworker.unit.infrastructure.adapter.repository.redis;

import com.apexglobal.test.orderworker.domain.model.OrderError;
import com.apexglobal.test.orderworker.infrastructure.adapter.repository.redis.OrderErrorRepositoryImpl;
import com.apexglobal.test.orderworker.util.GenerateDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderErrorRepositoryImplUnitTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private ListOperations<String, Object> listOperations;

    @InjectMocks
    private OrderErrorRepositoryImpl orderErrorRepository;

    private GenerateDataUtil generateDataUtil;

    @BeforeEach
    void setUp() {
        generateDataUtil = new GenerateDataUtil();
    }

    @Test
    void givenOrder_whenSave_thenShouldStoreOrder() {
        // Given
        String givenOrder = "givenOrder123";
        OrderError order = generateDataUtil.createOrderError(givenOrder);
        
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.opsForList()).thenReturn(listOperations);

        // When
        orderErrorRepository.save(order);

        // Then
        verify(valueOperations).set(order.getOrderId(), order);
        verify(listOperations).rightPush("order_id_list", order.getOrderId());
    }

    @Test
    void givengivenOrder_whenFindById_thenReturnOrder() {
        // Given
        String givenOrder = "givenOrder123";
        OrderError order = generateDataUtil.createOrderError(givenOrder);;

        when(valueOperations.get(givenOrder)).thenReturn(order);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // When
        Optional<OrderError> foundOrder = orderErrorRepository.findById(givenOrder);

        // Then
        assertTrue(foundOrder.isPresent());
        assertEquals(order, foundOrder.get());
        verify(valueOperations).get(givenOrder);
    }

    @Test
    void givengivenOrder_whenDelete_thenShouldRemoveOrder() {
        // Given
        String givenOrder = "givenOrder123";
        when(redisTemplate.opsForList()).thenReturn(listOperations);

        // When
        orderErrorRepository.delete(givenOrder);

        // Then
        verify(redisTemplate).delete(givenOrder);
        verify(listOperations).remove("order_id_list", 1, givenOrder);
    }

    @Test
    void whenFindAll_thenReturnAllOrders() {
        // Given

        when(listOperations.range("order_id_list", 0, -1)).thenReturn(Arrays.asList("givenOrder123", "givenOrder456"));
        when(valueOperations.get("givenOrder123")).thenReturn(generateDataUtil.createOrderError("givenOrder123"));
        when(valueOperations.get("givenOrder456")).thenReturn(generateDataUtil.createOrderError("givenOrder456"));
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.opsForList()).thenReturn(listOperations);

        // When
        List<OrderError> allOrders = orderErrorRepository.findAll();

        // Then
        assertEquals(2, allOrders.size());
        verify(listOperations).range("order_id_list", 0, -1);
    }

    @Test
    void whenFindAll_thenReturnEmptyList() {
        // Given
        when(listOperations.range("order_id_list", 0, -1)).thenReturn(null);
        when(redisTemplate.opsForList()).thenReturn(listOperations);

        // When
        List<OrderError> allOrders = orderErrorRepository.findAll();

        // Then
        assertEquals(0, allOrders.size());
        verify(listOperations).range("order_id_list", 0, -1);
    }
}
