package com.apexglobal.test.orderworker.unit.application.service;

import com.apexglobal.test.orderworker.application.dto.OrderErrorDTO;
import com.apexglobal.test.orderworker.application.dto.OrderErrorMapper;
import com.apexglobal.test.orderworker.application.service.OrderErrorService;
import com.apexglobal.test.orderworker.domain.exception.OrderNotFoundException;
import com.apexglobal.test.orderworker.domain.model.OrderError;
import com.apexglobal.test.orderworker.domain.port.outgoing.OrderErrorRepositoryPort;
import com.apexglobal.test.orderworker.util.GenerateDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderErrorServiceUnitTest {

    @Mock
    private OrderErrorRepositoryPort orderErrorRepository;

    @Mock
    private OrderErrorMapper mapper;

    @InjectMocks
    private OrderErrorService orderErrorService;

    private OrderError orderError;
    private OrderErrorDTO orderErrorDTO;
    private GenerateDataUtil generateDataUtil;

    @BeforeEach
    void setUp() {
        generateDataUtil = new GenerateDataUtil();
        orderError = generateDataUtil.createOrderError("order123");
        orderErrorDTO = generateDataUtil.createOrderErrorDTO("order123");
    }

    @Test
    void givenOrder_whenSave_thenOrderIsSaved() {
        // Given
        when(orderErrorRepository.findById(orderError.getOrderId())).thenReturn(Optional.empty());

        // When
        orderErrorService.save(orderError);

        // Then
        verify(orderErrorRepository).save(orderError);
    }

    @Test
    void givenExistingOrder_whenSave_thenOrderIsUpdated() {
        // Given
        when(orderErrorRepository.findById(orderError.getOrderId())).thenReturn(Optional.of(orderError));

        // When
        orderErrorService.save(orderError);

        // Then
        verify(orderErrorRepository).delete(orderError.getOrderId());
        verify(orderErrorRepository).save(orderError);
    }

    @Test
    void givenOrderId_whenFindById_thenReturnOrderErrorDTO() {
        // Given
        when(orderErrorRepository.findById(orderError.getOrderId())).thenReturn(Optional.of(orderError));
        when(mapper.toDto(orderError)).thenReturn(orderErrorDTO);

        // When
        Optional<OrderErrorDTO> result = orderErrorService.findById(orderError.getOrderId());

        // Then
        assertTrue(result.isPresent());
        assertEquals(orderErrorDTO, result.get());
    }

    @Test
    void givenNonExistingOrderId_whenFindById_thenThrowOrderNotFoundException() {
        // Given
        when(orderErrorRepository.findById("nonExistingOrderId")).thenReturn(Optional.empty());

        // When / Then
        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> {
            orderErrorService.findById("nonExistingOrderId");
        });
        assertEquals("Order not found nonExistingOrderId", exception.getMessage());
    }

    @Test
    void whenFindAll_thenReturnListOfOrderErrorDTOs() {
        // Given
        OrderError anotherOrderError = generateDataUtil.createOrderError("order456");
        when(orderErrorRepository.findAll()).thenReturn(Arrays.asList(orderError, anotherOrderError));
        when(mapper.toDto(orderError)).thenReturn(orderErrorDTO);
        when(mapper.toDto(anotherOrderError)).thenReturn(generateDataUtil.createOrderErrorDTO("order456"));

        // When
        var result = orderErrorService.findAll();

        // Then
        assertEquals(2, result.size());
        assertEquals(orderErrorDTO, result.get(0));
    }

    @Test
    void givenOrderId_whenDelete_thenOrderIsDeleted() {
        // Given
        String orderId = "order123";

        // When
        orderErrorService.delete(orderId);

        // Then
        verify(orderErrorRepository).delete(orderId);
    }
}
