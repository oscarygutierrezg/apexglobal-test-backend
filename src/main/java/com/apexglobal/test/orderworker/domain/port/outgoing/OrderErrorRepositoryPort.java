package com.apexglobal.test.orderworker.domain.port.outgoing;

import com.apexglobal.test.orderworker.domain.model.OrderError;

import java.util.List;
import java.util.Optional;

public interface OrderErrorRepositoryPort {

    void save(OrderError order);

    Optional<OrderError> findById(String orderId);

    List<OrderError> findAll();

    void delete(String orderId);

}
