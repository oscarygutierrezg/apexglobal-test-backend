package com.apexglobal.test.orderworker.infrastructure.adapter.repository.database;

import com.apexglobal.test.orderworker.domain.model.Order;
import com.apexglobal.test.orderworker.domain.port.outgoing.OrderRepositoryPort;
import com.apexglobal.test.orderworker.infrastructure.adapter.repository.mongo.OrderMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryPort {

    private final OrderMongoRepository orderRepository;

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> findById(String id) {
        return orderRepository.findByOrderId(id);
    }

}