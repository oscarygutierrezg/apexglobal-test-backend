package com.apexglobal.test.orderworker.unit.infrastructure.adapter.repository.rest;

import com.apexglobal.test.orderworker.application.dto.ProductDTO;
import com.apexglobal.test.orderworker.application.dto.ProductMapper;
import com.apexglobal.test.orderworker.domain.model.Product;
import com.apexglobal.test.orderworker.infrastructure.adapter.api.ProductFeignClient;
import com.apexglobal.test.orderworker.infrastructure.adapter.repository.rest.ProductRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductRepositoryImplUnitTest {

    @Mock
    private ProductFeignClient productFeignClient;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductRepositoryImpl productRepository;

    private Product product;
    private ProductDTO productDTO;
    private String productId;

    @BeforeEach
    void setUp() {
        product = new Product(); // Asegúrate de inicializar correctamente el objeto Product
        productId = "product123";
        product.setProductId(productId);


        productDTO = new ProductDTO(); // Asegúrate de inicializar correctamente el objeto Product
        productId = "product123";
        productDTO.setId(productId);
    }

    @Test
    void givenValidIds_whenFindAll_thenReturnProductList() {
        // Given
        List<String> ids = List.of(productId);
        List<ProductDTO> productList = List.of(productDTO);

        // Simula la respuesta del cliente Feign
        when(productFeignClient.multiple(ids)).thenReturn(productList);
        when(productMapper.toModel(productDTO)).thenReturn(product); // Simula el mapeo del DTO al modelo

        // When
        List<Product> result = productRepository.findAll(ids);

        // Then
        assertEquals(1, result.size());
        assertEquals(productId, result.get(0).getProductId());
        verify(productFeignClient).multiple(ids); // Verifica que el método fue llamado
        verify(productMapper).toModel(productDTO); // Verifica que se llamó al mapeador
    }

    @Test
    void givenValidId_whenFindById_thenReturnProduct() {
        // Given
        when(productFeignClient.index(productId)).thenReturn(productDTO); // Simula la respuesta del cliente Feign
        when(productMapper.toModel(productDTO)).thenReturn(product); // Simula el mapeo

        // When
        Optional<Product> result = productRepository.findById(productId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(productId, result.get().getProductId());
        verify(productFeignClient).index(productId); // Verifica que el método fue llamado
    }

    @Test
    void givenInvalidId_whenFindById_thenReturnEmpty() {
        // Given
        when(productFeignClient.index(anyString())).thenThrow(new RuntimeException("Product not found")); // Simula una excepción

        // When
        Optional<Product> result = productRepository.findById(productId);

        // Then
        assertTrue(result.isEmpty());
        verify(productFeignClient).index(productId); // Verifica que el método fue llamado
    }
}
