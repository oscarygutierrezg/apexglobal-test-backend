package com.apexglobal.test.orderworker.application.service;

import com.apexglobal.test.orderworker.domain.port.incoming.LockResourceUseCase;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LockResourceService implements LockResourceUseCase {

    private final RedissonClient redissonClient;

    @Override
    public void executeWithLock(String resourceKey, Runnable runnable) throws InterruptedException {
        String lockKey = "resource-lock:" + resourceKey;

        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (lock.tryLock()) {
                try {
                    runnable.run();
                } finally {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            } else {
                System.out.println("The resource " + resourceKey + " is already being processed.");
            }
        } catch (Exception e) {
            System.out.println("Error al procesar el pedido: " + e.getMessage());
            throw e;
        }
    }
}
