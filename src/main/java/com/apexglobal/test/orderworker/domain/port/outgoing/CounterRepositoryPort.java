package com.apexglobal.test.orderworker.domain.port.outgoing;

public interface CounterRepositoryPort {

    Long incrementCounter(String key);

    Long decrementCounter(String key);

    Long getCounterValue(String key);
}