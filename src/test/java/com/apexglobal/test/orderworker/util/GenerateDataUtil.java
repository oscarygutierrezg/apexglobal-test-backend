package com.apexglobal.test.orderworker.util;

import com.apexglobal.test.orderworker.application.dto.CustomerDTO;
import com.apexglobal.test.orderworker.application.dto.OrderDTO;
import com.apexglobal.test.orderworker.application.dto.OrderErrorDTO;
import com.apexglobal.test.orderworker.application.dto.OrderMessageDTO;
import com.apexglobal.test.orderworker.application.dto.ProductDTO;
import com.apexglobal.test.orderworker.domain.model.Order;
import com.apexglobal.test.orderworker.domain.model.OrderError;
import com.apexglobal.test.orderworker.domain.model.Product;
import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenerateDataUtil {

    private Faker faker = new Faker();

    public Order createOrder() {
        Order order = new Order();
        order.setOrderId(faker.commerce().promotionCode());
        order.setCustomerId(faker.commerce().promotionCode());
        order.setId(faker.commerce().promotionCode());
        order.setProducts(createProductList(5));
        return order;
    }

    public OrderDTO createOrderDTO() {
        OrderDTO order = new OrderDTO();
        order.setOrderId(faker.commerce().promotionCode());
        order.setCustomerId(faker.commerce().promotionCode());
        order.setProducts(createProductDTOList(5));
        return order;
    }

    public OrderError createOrderError(String orderId) {
        OrderError order = new OrderError();
        order.setOrderId(orderId);
        order.setOrderId(faker.commerce().promotionCode());
        order.setCustomerId(faker.commerce().promotionCode());
        return order;
    }

    public OrderErrorDTO createOrderErrorDTO(String orderId) {
        OrderErrorDTO order = new OrderErrorDTO();
        order.setOrderId(orderId);
        order.setOrderId(faker.commerce().promotionCode());
        order.setCustomerId(faker.commerce().promotionCode());
        return order;
    }

    private List<Product> createProductList(int size) {
        List<Product> products = new ArrayList<>();
        for (int j = 0; j < size; j++) {
            products.add(Product.builder()
                    .price(generateRandomPrice(faker, 100, 10000))
                    .name(faker.beer().name())
                    .description(faker.beer().name())
                    .productId(faker.commerce().promotionCode()).build());
        }
        return products;
    }

    private List<ProductDTO> createProductDTOList(int size) {
        List<ProductDTO> products = new ArrayList<>();
        for (int j = 0; j < size; j++) {
            products.add(ProductDTO.builder()
                    .price(generateRandomPrice(faker, 100, 10000))
                    .name(faker.beer().name())
                    .description(faker.beer().name())
                    .id(faker.commerce().promotionCode()).build());
        }
        return products;
    }


    public Product createProduct() {
        return  Product.builder()
                    .price(generateRandomPrice(faker, 100, 10000))
                    .name(faker.beer().name())
                    .description(faker.beer().name())
                    .productId(faker.commerce().promotionCode()).build();
    }

    public ProductDTO createProductDTO() {
        return ProductDTO.builder()
                    .price(generateRandomPrice(faker, 100, 10000))
                    .name(faker.beer().name())
                    .description(faker.beer().name())
                    .id(faker.commerce().promotionCode()).build();
    }


    public static double generateRandomPrice(Faker faker, int min, int max) {
        int randomNumber = faker.number().numberBetween(min, max);

        BigDecimal price = BigDecimal.valueOf(randomNumber + faker.number().randomDouble(2, 0, 99))
                ;

        return price.doubleValue();
    }


    public OrderMessageDTO createOrderMessageDto() {
        return OrderMessageDTO
                .builder()
                .orderId(faker.commerce().promotionCode())
                .customerId(faker.commerce().promotionCode())
                .products(Arrays.asList("1","2","3")).build();
    }

    public OrderMessageDTO createCorrectOrderMessageDto() {
        return OrderMessageDTO
                .builder()
                .orderId(faker.commerce().promotionCode())
                .customerId("b7fc4cb6-6844-4cd0-95fb-f424a3938eb4")
                .products(Arrays.asList("9023995c-72af-4170-b449-482ec5df146b")).build();
    }

    public CustomerDTO  createCustomerDTO(boolean active) {
        return CustomerDTO
                .builder()
                .name(faker.app().name())
                .active(active)
                .build();
    }

    public OrderMessageDTO createOrderMessageDtoWithNonExistentProduct() {
        return OrderMessageDTO
                .builder()
                .orderId(faker.commerce().promotionCode())
                .customerId("b7fc4cb6-6844-4cd0-95fb-f424a3938eb4")
                .products(Arrays.asList("1")).build();
    }

    public OrderMessageDTO createOrderMessageDtoWithInactiveCustomer() {
        return OrderMessageDTO
                .builder()
                .orderId(faker.commerce().promotionCode())
                .customerId("7ac66acd-411d-4d4b-9cc9-4f9cbe5425f7")
                .products(Arrays.asList("9023995c-72af-4170-b449-482ec5df146b")).build();
    }
}
