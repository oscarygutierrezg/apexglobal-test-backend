package com.apexglobal.test.orderworker.domain.port.incoming;

import com.apexglobal.test.orderworker.application.dto.OrderMessageDTO;

public interface OrderProcessingUseCase {

    void processOrder(OrderMessageDTO order) throws InterruptedException;
}
