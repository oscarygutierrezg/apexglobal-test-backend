package com.apexglobal.test.orderworker.unit.infrastructure.adapter.repository.database;

import com.apexglobal.test.orderworker.domain.model.Order;
import com.apexglobal.test.orderworker.infrastructure.adapter.repository.database.OrderRepositoryImpl;
import com.apexglobal.test.orderworker.infrastructure.adapter.repository.mongo.OrderMongoRepository;
import com.apexglobal.test.orderworker.util.GenerateDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryImplUnitTest {

    @Mock
    private OrderMongoRepository orderMongoRepository;

    @InjectMocks
    private OrderRepositoryImpl orderRepositoryImpl;

    private GenerateDataUtil generateDataUtil;

    @BeforeEach
    void setUp() {
        generateDataUtil = new GenerateDataUtil(); // Inicializar el util en el setup
    }

    @Test
    void givenValidOrderId_whenFindByOrderId_thenReturnOrder() {
        // Given
        Order expectedOrder = generateDataUtil.createOrder();
        when(orderMongoRepository.findByOrderId(expectedOrder.getOrderId())).thenReturn(Optional.of(expectedOrder));

        // When
        Optional<Order> actualOrder = orderRepositoryImpl.findById(expectedOrder.getOrderId());

        // Then
        assertNotNull(actualOrder);
        assertTrue(actualOrder.isPresent());
        assertEquals(expectedOrder, actualOrder.get());
        verify(orderMongoRepository).findByOrderId(expectedOrder.getOrderId());
    }

    @Test
    void givenInvalidOrderId_whenFindByOrderId_thenReturnEmpty() {
        // Given
        String invalidOrderId = "invalid-order-id";
        when(orderMongoRepository.findByOrderId(invalidOrderId)).thenReturn(Optional.empty());

        // When
        Optional<Order> actualOrder = orderRepositoryImpl.findById(invalidOrderId);

        // Then
        assertTrue(actualOrder.isEmpty());
        verify(orderMongoRepository).findByOrderId(invalidOrderId);
    }

    @Test
    void givenOrder_whenSaveOrder_thenReturnSavedOrder() {
        // Given
        Order expectedOrder = generateDataUtil.createOrder();
        when(orderMongoRepository.save(any(Order.class))).thenReturn(expectedOrder);

        // When
        Order actualOrder = orderRepositoryImpl.save(expectedOrder);

        // Then
        assertNotNull(actualOrder);
        assertEquals(expectedOrder, actualOrder);
        verify(orderMongoRepository).save(expectedOrder);
    }
}
