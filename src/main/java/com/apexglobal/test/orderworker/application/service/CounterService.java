package com.apexglobal.test.orderworker.application.service;


import com.apexglobal.test.orderworker.domain.port.incoming.CounterUseCase;
import com.apexglobal.test.orderworker.domain.port.outgoing.CounterRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CounterService implements CounterUseCase {

    private final CounterRepositoryPort counterRepository;

    @Override
    public Long incrementCounter(String key) {
        return counterRepository.incrementCounter(key);
    }
    @Override
    public Long decrementCounter(String key) {
        return counterRepository.decrementCounter(key);
    }
    @Override
    public Long getCounterValue(String key) {
        return counterRepository.getCounterValue(key);
    }
}
