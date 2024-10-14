package com.apexglobal.test.orderworker.infrastructure.adapter.repository.redis;

import com.apexglobal.test.orderworker.domain.port.outgoing.CounterRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CounterRepositoryImpl implements CounterRepositoryPort {

    private final RedisTemplate<String, Integer> redisTemplateCounter;

    @Override
    public Long incrementCounter(String key) {
        return redisTemplateCounter.opsForValue().increment("counter_"+key, 1L);
    }

    @Override
    public Long decrementCounter(String key) {
        return redisTemplateCounter.opsForValue().decrement("counter_"+key, 1L);
    }

    @Override
    public Long getCounterValue(String key) {
        Integer value = redisTemplateCounter.opsForValue().get("counter_"+key);
        return (long) (value != null ? value : 0);
    }
}