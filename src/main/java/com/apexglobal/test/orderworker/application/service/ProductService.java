package com.apexglobal.test.orderworker.application.service;

import com.apexglobal.test.orderworker.domain.model.Product;
import com.apexglobal.test.orderworker.domain.port.incoming.ProductUseCase;
import com.apexglobal.test.orderworker.domain.port.outgoing.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public List<Product> findAll(List<String> ids) {
        return productRepositoryPort.findAll(ids);
    }

    @Override
    public Optional<Product> findById(String id) {
        return productRepositoryPort.findById(id);
    }
}