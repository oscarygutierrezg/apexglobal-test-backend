package com.apexglobal.test.orderworker.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager customersCacheManager() {
        return new ConcurrentMapCacheManager("customers-cache","products-cache","products-multiple-cache");
    }
}
