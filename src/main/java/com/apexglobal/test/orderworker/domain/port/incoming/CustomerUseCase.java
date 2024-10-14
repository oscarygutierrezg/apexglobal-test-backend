package com.apexglobal.test.orderworker.domain.port.incoming;

import com.apexglobal.test.orderworker.application.dto.CustomerDTO;
import com.apexglobal.test.orderworker.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface CustomerUseCase {
    Optional<CustomerDTO> findById(String id);

}