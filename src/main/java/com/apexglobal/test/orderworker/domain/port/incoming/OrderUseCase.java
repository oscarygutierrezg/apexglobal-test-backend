package com.apexglobal.test.orderworker.domain.port.incoming;

import com.apexglobal.test.orderworker.application.dto.OrderDTO;
import com.apexglobal.test.orderworker.domain.model.Order;

public interface OrderUseCase {

    OrderDTO create(Order order);

    OrderDTO findById(String id);

}