package com.apexglobal.test.orderworker.application.controller;


import com.apexglobal.test.orderworker.application.dto.OrderErrorDTO;
import com.apexglobal.test.orderworker.domain.port.incoming.OrderErrorUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/orders/reprocess")
@RequiredArgsConstructor
public class OrderReprocessController {

    private final OrderErrorUseCase orderErrorService;

    @GetMapping("/pending")
    public ResponseEntity<List<OrderErrorDTO>> findAll() {
        return ResponseEntity.ok(orderErrorService.findAll());
    }
}