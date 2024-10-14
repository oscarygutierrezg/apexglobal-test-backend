package com.apexglobal.test.orderworker.infrastructure.adapter.api;

import com.apexglobal.test.orderworker.application.dto.ProductDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "productFeignClient", url = "${feign.product-api.url}")
public interface ProductFeignClient {

    @GetMapping("/v1/products/{id}")
    @Cacheable(cacheNames = "products-cache", key = "#id")
    ProductDTO index(@PathVariable("id") String id);

    @PostMapping("/v1/products/multiple")
    @Cacheable(cacheNames = "products-multiple-cache", key = "#ids")
    List<ProductDTO> multiple(@RequestBody List<String> ids);
}