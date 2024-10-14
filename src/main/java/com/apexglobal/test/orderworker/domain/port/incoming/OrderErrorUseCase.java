package com.apexglobal.test.orderworker.domain.port.incoming;

import com.apexglobal.test.orderworker.application.dto.OrderErrorDTO;
import com.apexglobal.test.orderworker.domain.model.OrderError;

import java.util.List;
import java.util.Optional;

public interface OrderErrorUseCase {

    void save(OrderError order);

    Optional<OrderErrorDTO> findById(String orderId);

    List<OrderErrorDTO> findAll();

    void delete(String orderId);

}
