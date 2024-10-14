package com.apexglobal.test.orderworker.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderError {

    private String orderId;
    private String customerId;
    private String error;
    private Long retries;
    private List<String> productIds;
}
