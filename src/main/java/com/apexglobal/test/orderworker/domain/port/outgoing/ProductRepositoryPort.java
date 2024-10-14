package com.apexglobal.test.orderworker.domain.port.outgoing;


import com.apexglobal.test.orderworker.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {
    List<Product> findAll(List<String> ids);
    Optional<Product> findById(String id);
}
