package com.apexglobal.test.orderworker.application.service;

import com.apexglobal.test.orderworker.application.dto.OrderErrorDTO;
import com.apexglobal.test.orderworker.application.dto.OrderErrorMapper;
import com.apexglobal.test.orderworker.domain.exception.OrderNotFoundException;
import com.apexglobal.test.orderworker.domain.model.OrderError;
import com.apexglobal.test.orderworker.domain.port.incoming.OrderErrorUseCase;
import com.apexglobal.test.orderworker.domain.port.outgoing.OrderErrorRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderErrorService implements OrderErrorUseCase {

    private final OrderErrorRepositoryPort orderErrorRepository;
    private final OrderErrorMapper mapper;

    @Override
    public void save(OrderError order) {
        Optional<OrderError> orderError = orderErrorRepository.findById(order.getOrderId());
        if (orderError.isPresent()){
            orderErrorRepository.delete(order.getOrderId());
        }
        orderErrorRepository.save(order);
    }

    @Override
    public Optional<OrderErrorDTO> findById(String orderId) {
        Optional<OrderError> orderError = orderErrorRepository.findById(orderId);
        if (orderError.isPresent()){
            return Optional.of(mapper.toDto(orderError.get()));
        } else {
            throw  new OrderNotFoundException("Order not found " + orderId);
        }
    }

    @Override
    public List<OrderErrorDTO> findAll() {
        return orderErrorRepository.findAll().stream().map(mapper::toDto).toList();
    }

    @Override
    public void delete(String orderId) {
        orderErrorRepository.delete(orderId);
    }
}