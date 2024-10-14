package com.apexglobal.test.orderworker.integration.application.controller;

import com.apexglobal.test.orderworker.OrderWorkerApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.apexglobal.test.orderworker.application.dto.OrderDTO;
import com.apexglobal.test.orderworker.domain.port.incoming.OrderUseCase;
import com.apexglobal.test.orderworker.util.GenerateDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = OrderWorkerApplication.class)
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderUseCase orderUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private final GenerateDataUtil generateDataUtil = new GenerateDataUtil();

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_GetOrderById_Should_ReturnOrder_When_ValidId() throws Exception {
        // Given
        OrderDTO orderDTO = orderUseCase.create(generateDataUtil.createOrder());

        // When
        ResultActions res = mockMvc.perform(
                        MockMvcRequestBuilders.get("/v1/orders/" + orderDTO.getOrderId())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Then
        OrderDTO result = objectMapper.readValue(res.andReturn().getResponse().getContentAsString(), OrderDTO.class);
        assertNotNull(result);
        assertEquals(orderDTO.getOrderId(), result.getOrderId(), "Los ID de orden deben coincidir");
    }

    @Test
    void test_GetOrderById_Should_ReturnNotFound_When_InvalidId() throws Exception {
        // When
        ResultActions res = mockMvc.perform(
                        MockMvcRequestBuilders.get("/v1/orders/invalid-id")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}

