package com.apexglobal.test.orderworker.domain.port.incoming;

public interface CounterUseCase {

    Long incrementCounter(String key);

    Long decrementCounter(String key);

    Long getCounterValue(String key);
}