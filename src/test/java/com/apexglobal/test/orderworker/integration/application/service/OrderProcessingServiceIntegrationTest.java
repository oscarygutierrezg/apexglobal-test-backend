package com.apexglobal.test.orderworker.integration.application.service;

import com.apexglobal.test.orderworker.application.dto.CustomerDTO;
import com.apexglobal.test.orderworker.application.dto.OrderDTO;
import com.apexglobal.test.orderworker.application.dto.OrderMessageDTO;
import com.apexglobal.test.orderworker.application.exception.AlreadyBeenProcessedException;
import com.apexglobal.test.orderworker.application.exception.MaxRetryException;
import com.apexglobal.test.orderworker.domain.exception.CustomerNotFoundOrInactiveException;
import com.apexglobal.test.orderworker.domain.exception.ProductNotFoundException;
import com.apexglobal.test.orderworker.domain.port.incoming.CounterUseCase;
import com.apexglobal.test.orderworker.domain.port.incoming.OrderProcessingUseCase;
import com.apexglobal.test.orderworker.domain.port.incoming.OrderUseCase;
import com.apexglobal.test.orderworker.util.GenerateDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext
@AutoConfigureMockMvc
@EnableCaching
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderProcessingServiceIntegrationTest {

    @Autowired
    private OrderProcessingUseCase orderProcessingService;

    @Autowired
    private OrderUseCase orderUseCase;

    @Autowired
    private CounterUseCase counterService;

    private GenerateDataUtil generateDataUtil;

    @Value("${spring.kafka.consumer.topics.order-topic}")
    private String topic;

    @BeforeEach
    public void setUp() {
        generateDataUtil = new GenerateDataUtil();
    }

    @Test
    public void givenValidOrderMessage_whenProcessOrder_thenOrderShouldBeSaved() throws InterruptedException {
        // Given
        OrderMessageDTO orderMessage = generateDataUtil.createCorrectOrderMessageDto();

        // When
        orderProcessingService.processOrder(orderMessage);

        // Then
        OrderDTO result = orderUseCase.findById(orderMessage.getOrderId());
        assertNotNull(result, "El resultado no debe ser nulo");
        assertEquals(orderMessage.getOrderId(), result.getOrderId(), "Los ID de orden deben coincidir");
    }

    @Test
    public void givenOrderMessageWithInactiveCustomer_whenProcessOrder_thenShouldThrowCustomerNotFoundOrInactiveException() {
        // Given
        OrderMessageDTO orderMessage = generateDataUtil.createOrderMessageDtoWithInactiveCustomer();

        // When & Then
        assertThrows(CustomerNotFoundOrInactiveException.class, () -> {
            orderProcessingService.processOrder(orderMessage);
        });
    }

    @Test
    public void givenOrderMessageWithNonExistentProduct_whenProcessOrder_thenShouldThrowProductNotFoundException() {
        // Given
        OrderMessageDTO orderMessage = generateDataUtil.createOrderMessageDtoWithNonExistentProduct();

        // When & Then
        assertThrows(ProductNotFoundException.class, () -> {
            orderProcessingService.processOrder(orderMessage);
        });
    }

    @Test
    public void givenOrderMessageAlreadyProcessed_whenProcessOrder_thenShouldThrowAlreadyBeenProcessedException() throws InterruptedException {
        // Given
        OrderMessageDTO orderMessage = generateDataUtil.createCorrectOrderMessageDto();
        orderProcessingService.processOrder(orderMessage); // Procesar primero para marcar como ya procesada

        // When & Then
        assertThrows(AlreadyBeenProcessedException.class, () -> {
            orderProcessingService.processOrder(orderMessage);
        });
    }

    @Test
    public void givenOrderMessageWithMaxRetries_whenProcessOrder_thenShouldThrowMaxRetryException() throws InterruptedException {
        // Given
        OrderMessageDTO orderMessage = generateDataUtil.createCorrectOrderMessageDto();
        for (int i = 0; i <100; i++) {
            counterService.incrementCounter(orderMessage.getOrderId());
        }

        // When & Then
        assertThrows(MaxRetryException.class, () -> {
            orderProcessingService.processOrder(orderMessage);
        });
    }
}
