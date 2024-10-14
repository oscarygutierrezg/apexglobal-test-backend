package com.apexglobal.test.orderworker.application.service;

import com.apexglobal.test.orderworker.application.dto.CustomerDTO;
import com.apexglobal.test.orderworker.domain.port.incoming.CustomerUseCase;
import com.apexglobal.test.orderworker.domain.port.outgoing.CustomerRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService implements CustomerUseCase {

    private final CustomerRepositoryPort customerRepositoryPort;

    @Override
    public Optional<CustomerDTO> findById(String id) {
        return customerRepositoryPort.findById(id);
    }
}