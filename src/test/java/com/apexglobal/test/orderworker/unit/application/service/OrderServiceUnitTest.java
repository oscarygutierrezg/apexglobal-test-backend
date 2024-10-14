package com.apexglobal.test.orderworker.unit.application.service;

import com.apexglobal.test.orderworker.application.dto.OrderDTO;
import com.apexglobal.test.orderworker.application.dto.OrderMapper;
import com.apexglobal.test.orderworker.application.service.OrderService;
import com.apexglobal.test.orderworker.domain.exception.OrderNotFoundException;
import com.apexglobal.test.orderworker.domain.model.Order;
import com.apexglobal.test.orderworker.domain.port.outgoing.OrderRepositoryPort;
import com.apexglobal.test.orderworker.util.GenerateDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceUnitTest {

    @Mock
    private OrderRepositoryPort orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private OrderDTO orderDTO;

    private GenerateDataUtil generateDataUtil;

    @BeforeEach
    void setUp() {
        generateDataUtil = new GenerateDataUtil();
        order  = generateDataUtil.createOrder();
        orderDTO = generateDataUtil.createOrderDTO();
    }

    @Test
    void givenOrder_whenCreate_thenReturnOrderDTO() {
        // Given
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderDTO);

        // When
        OrderDTO result = orderService.create(order);

        // Then
        assertEquals(orderDTO, result);
        verify(orderRepository).save(order);
        verify(orderMapper).toDto(order);
    }

    @Test
    void givenOrderId_whenFindById_thenReturnOrderDTO() {
        // Given
        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDTO);

        // When
        OrderDTO result = orderService.findById(order.getOrderId());

        // Then
        assertEquals(orderDTO, result);
        verify(orderRepository).findById(order.getOrderId());
        verify(orderMapper).toDto(order);
    }

    @Test
    void givenNonExistingOrderId_whenFindById_thenThrowOrderNotFoundException() {
        // Given
        when(orderRepository.findById("nonExistingOrderId")).thenReturn(Optional.empty());

        // When / Then
        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> {
            orderService.findById("nonExistingOrderId");
        });
        assertEquals("Order not found nonExistingOrderId", exception.getMessage());
        verify(orderRepository).findById("nonExistingOrderId");
    }
}
