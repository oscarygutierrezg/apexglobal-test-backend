package com.apexglobal.test.orderworker.unit.infrastructure.adapter.repository.rest;

import com.apexglobal.test.orderworker.application.dto.CustomerDTO;
import com.apexglobal.test.orderworker.infrastructure.adapter.api.CustomerFeignClient;
import com.apexglobal.test.orderworker.infrastructure.adapter.repository.rest.CustomerRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerRepositoryImplUnitTest {

    @Mock
    private CustomerFeignClient customerFeignClient;

    @InjectMocks
    private CustomerRepositoryImpl customerRepository;

    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customerDTO = new CustomerDTO(); // Asegúrate de inicializar correctamente el objeto DTO
        customerDTO.setId("customer123");
        customerDTO.setName("John Doe"); // Ajusta según las propiedades de tu CustomerDTO
    }

    @Test
    void givenValidId_whenFindById_thenReturnCustomerDTO() {
        // Given
        String id = "customer123";
        when(customerFeignClient.index(id)).thenReturn(customerDTO); // Simula la respuesta del cliente Feign

        // When
        Optional<CustomerDTO> result = customerRepository.findById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(customerDTO, result.get());
        verify(customerFeignClient).index(id); // Verifica que el método fue llamado
    }

    @Test
    void givenInvalidId_whenFindById_thenReturnEmpty() {
        // Given
        String id = "invalidId";
        when(customerFeignClient.index(anyString())).thenThrow(new RuntimeException("Customer not found")); // Simula una excepción

        // When
        Optional<CustomerDTO> result = customerRepository.findById(id);

        // Then
        assertTrue(result.isEmpty());
        verify(customerFeignClient).index(id); // Verifica que el método fue llamado
    }
}
