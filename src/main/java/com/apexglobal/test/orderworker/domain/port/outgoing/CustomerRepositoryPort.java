package com.apexglobal.test.orderworker.domain.port.outgoing;


import com.apexglobal.test.orderworker.application.dto.CustomerDTO;
import com.apexglobal.test.orderworker.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface CustomerRepositoryPort {
    Optional<CustomerDTO> findById(String id);
}
