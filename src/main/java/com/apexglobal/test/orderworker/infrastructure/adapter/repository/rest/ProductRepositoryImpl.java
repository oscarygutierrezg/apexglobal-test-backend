package com.apexglobal.test.orderworker.infrastructure.adapter.repository.rest;

import com.apexglobal.test.orderworker.application.dto.ProductMapper;
import com.apexglobal.test.orderworker.domain.model.Product;
import com.apexglobal.test.orderworker.domain.port.outgoing.ProductRepositoryPort;
import com.apexglobal.test.orderworker.infrastructure.adapter.api.ProductFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductRepositoryImpl implements ProductRepositoryPort {

    private final ProductFeignClient productFeignClient;
    private final ProductMapper productMapper;

    @Override
    public List<Product> findAll(List<String> ids) {
        return productFeignClient.multiple(ids)
                .stream()
                .map(productMapper::toModel)
                .toList();
    }

    @Override
    public Optional<Product> findById(String id) {
        try {
            Product product = productMapper.toModel(productFeignClient.index(id));
            return Optional.ofNullable(product);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }
}