package com.apexglobal.test.orderworker.application.service;

import com.apexglobal.test.orderworker.application.dto.OrderDTO;
import com.apexglobal.test.orderworker.application.dto.OrderMapper;
import com.apexglobal.test.orderworker.domain.exception.OrderNotFoundException;
import com.apexglobal.test.orderworker.domain.exception.ProductNotFoundException;
import com.apexglobal.test.orderworker.domain.model.Order;
import com.apexglobal.test.orderworker.domain.port.incoming.OrderUseCase;
import com.apexglobal.test.orderworker.domain.port.outgoing.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderUseCase {

    private final OrderRepositoryPort orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderDTO create(Order order) {
        return
                orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDTO findById(String orderId) {
        return  orderMapper.toDto(orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found " + orderId)));

    }
}