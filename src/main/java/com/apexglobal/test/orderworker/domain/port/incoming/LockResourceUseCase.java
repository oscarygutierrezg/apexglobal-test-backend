package com.apexglobal.test.orderworker.domain.port.incoming;

public interface LockResourceUseCase {

    void executeWithLock(String resourceKey, Runnable runnable) throws InterruptedException;

}
