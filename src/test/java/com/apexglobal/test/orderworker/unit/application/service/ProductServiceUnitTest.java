package com.apexglobal.test.orderworker.unit.application.service;

import com.apexglobal.test.orderworker.domain.model.Product;
import com.apexglobal.test.orderworker.domain.port.outgoing.ProductRepositoryPort;
import com.apexglobal.test.orderworker.application.service.ProductService;
import com.apexglobal.test.orderworker.util.GenerateDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceUnitTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;
    private GenerateDataUtil generateDataUtil;

    @BeforeEach
    void setUp() {
        generateDataUtil = new GenerateDataUtil();
        product1 = generateDataUtil.createProduct();
        product2 = generateDataUtil.createProduct();
    }

    @Test
    void givenProductIds_whenFindAll_thenReturnProductList() {
        // Given
        List<String> ids = Arrays.asList("product1", "product2");
        when(productRepositoryPort.findAll(ids)).thenReturn(Arrays.asList(product1, product2));

        // When
        List<Product> result = productService.findAll(ids);

        // Then
        assertEquals(2, result.size());
        assertEquals(product1, result.get(0));
        assertEquals(product2, result.get(1));
        verify(productRepositoryPort).findAll(ids);
    }

    @Test
    void givenValidProductId_whenFindById_thenReturnProduct() {
        // Given
        when(productRepositoryPort.findById("product1")).thenReturn(Optional.of(product1));

        // When
        Optional<Product> result = productService.findById("product1");

        // Then
        assertTrue(result.isPresent());
        assertEquals(product1, result.get());
        verify(productRepositoryPort).findById("product1");
    }

    @Test
    void givenInvalidProductId_whenFindById_thenReturnEmpty() {
        // Given
        when(productRepositoryPort.findById("invalidId")).thenReturn(Optional.empty());

        // When
        Optional<Product> result = productService.findById("invalidId");

        // Then
        assertFalse(result.isPresent());
        verify(productRepositoryPort).findById("invalidId");
    }
}

