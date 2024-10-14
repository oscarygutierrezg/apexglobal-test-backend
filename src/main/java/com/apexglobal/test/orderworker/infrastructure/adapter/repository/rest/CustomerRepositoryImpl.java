package com.apexglobal.test.orderworker.infrastructure.adapter.repository.rest;

import com.apexglobal.test.orderworker.application.dto.CustomerDTO;
import com.apexglobal.test.orderworker.application.dto.ProductMapper;
import com.apexglobal.test.orderworker.domain.model.Product;
import com.apexglobal.test.orderworker.domain.port.outgoing.CustomerRepositoryPort;
import com.apexglobal.test.orderworker.domain.port.outgoing.ProductRepositoryPort;
import com.apexglobal.test.orderworker.infrastructure.adapter.api.CustomerFeignClient;
import com.apexglobal.test.orderworker.infrastructure.adapter.api.ProductFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerRepositoryImpl implements CustomerRepositoryPort {

    private final CustomerFeignClient customerFeignClient;

    @Override
    public Optional<CustomerDTO> findById(String id) {
        try {
            return Optional.ofNullable(customerFeignClient.index(id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }
}