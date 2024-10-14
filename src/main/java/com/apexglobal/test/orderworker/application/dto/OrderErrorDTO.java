package com.apexglobal.test.orderworker.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderErrorDTO {

    private String orderId;
    private String customerId;
    private String error;
    private Long retries;
    private List<String> productIds;
}
