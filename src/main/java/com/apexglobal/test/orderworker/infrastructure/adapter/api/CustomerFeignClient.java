package com.apexglobal.test.orderworker.infrastructure.adapter.api;

import com.apexglobal.test.orderworker.application.dto.CustomerDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customerFeignClient", url = "${feign.customer-api.url}")
public interface CustomerFeignClient {

    @GetMapping("/v1/customers/{id}")
    @Cacheable(cacheNames = "customers-cache", key = "#id")
    CustomerDTO index(@PathVariable("id") String id);

}