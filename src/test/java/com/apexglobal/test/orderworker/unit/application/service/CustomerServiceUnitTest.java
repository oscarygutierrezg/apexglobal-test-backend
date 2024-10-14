package com.apexglobal.test.orderworker.unit.application.service;

import com.apexglobal.test.orderworker.application.dto.CustomerDTO;
import com.apexglobal.test.orderworker.application.service.CustomerService;
import com.apexglobal.test.orderworker.domain.port.outgoing.CustomerRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceUnitTest {

    @Mock
    private CustomerRepositoryPort customerRepositoryPort;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void givenValidId_whenFindById_thenReturnCustomer() {
        // Given
        String id = "customer123";
        CustomerDTO expectedCustomer = new CustomerDTO(); // You may want to set properties for your DTO
        when(customerRepositoryPort.findById(id)).thenReturn(Optional.of(expectedCustomer)); // Mocked return value

        // When
        Optional<CustomerDTO> actualCustomer = customerService.findById(id);

        // Then
        assertTrue(actualCustomer.isPresent());
        assertEquals(expectedCustomer, actualCustomer.get());
        verify(customerRepositoryPort).findById(id); // Verifies the interaction
    }

    @Test
    void givenInvalidId_whenFindById_thenReturnEmpty() {
        // Given
        String id = "nonExistentId";
        when(customerRepositoryPort.findById(id)).thenReturn(Optional.empty()); // Mocked return value

        // When
        Optional<CustomerDTO> actualCustomer = customerService.findById(id);

        // Then
        assertTrue(actualCustomer.isEmpty());
        verify(customerRepositoryPort).findById(id); // Verifies the interaction
    }
}
