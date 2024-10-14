package com.apexglobal.test.orderworker.domain.port.outgoing;

import com.apexglobal.test.orderworker.domain.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findById(String id);

}
