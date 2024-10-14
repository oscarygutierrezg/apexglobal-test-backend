package com.apexglobal.test.orderworker.integration.application.controller;

import com.apexglobal.test.orderworker.OrderWorkerApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.apexglobal.test.orderworker.application.dto.OrderErrorDTO;
import com.apexglobal.test.orderworker.domain.port.incoming.OrderErrorUseCase;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = OrderWorkerApplication.class)
class OrderReprocessControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderErrorUseCase orderErrorService;

    @Autowired
    private ObjectMapper objectMapper;

    private final GenerateDataUtil generateDataUtil = new GenerateDataUtil();

    @BeforeEach
    void setUp() {
        orderErrorService.save(generateDataUtil.createOrderError("131323"));
    }

    @Test
    void test_FindAll_Should_ReturnListOfOrderErrors_When_Called() throws Exception {
        // When
        ResultActions res = mockMvc.perform(
                        MockMvcRequestBuilders.get("/v1/orders/reprocess/pending")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Then
        List<OrderErrorDTO> result = objectMapper.readValue(res.andReturn().getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, OrderErrorDTO.class));

        assertNotNull(result, "La lista de errores no debe ser nula");
        assertFalse(result.isEmpty(), "La lista de errores debe contener al menos un elemento");
    }
}
