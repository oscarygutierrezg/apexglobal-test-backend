package com.apexglobal.test.orderworker.unit.application.service;

import com.apexglobal.test.orderworker.application.service.LockResourceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class LockResourceServiceUnitTest {

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock rLock;

    @InjectMocks
    private LockResourceService lockResourceService;

    @Test
    void givenResourceKey_whenExecuteWithLock_thenRunRunnable() throws InterruptedException {
        // Given
        String resourceKey = "resource123";
        Runnable runnable = mock(Runnable.class);
        when(redissonClient.getLock("resource-lock:" + resourceKey)).thenReturn(rLock);
        when(rLock.tryLock()).thenReturn(true); // Simula que se puede obtener el bloqueo

        // When
        lockResourceService.executeWithLock(resourceKey, runnable);

        // Then
        verify(rLock).tryLock(); // Verifica que se haya intentado adquirir el bloqueo
        verify(runnable).run(); // Verifica que se haya ejecutado el runnable
    }

    @Test
    void givenResourceKey_whenExecuteWithLockAndLockIsHeld_thenDoNotRunRunnable() throws InterruptedException {
        // Given
        String resourceKey = "resource123";
        Runnable runnable = mock(Runnable.class);
        when(redissonClient.getLock("resource-lock:" + resourceKey)).thenReturn(rLock);
        when(rLock.tryLock()).thenReturn(false); // Simula que no se puede obtener el bloqueo

        // When
        lockResourceService.executeWithLock(resourceKey, runnable);

        // Then
        verify(rLock).tryLock(); // Verifica que se haya intentado adquirir el bloqueo
        verify(runnable, never()).run(); // Verifica que el runnable no se ejecutó
        verify(rLock, never()).unlock(); // Verifica que el bloqueo no se liberó
    }

    @Test
    void givenResourceKey_whenExecuteWithLockThrowsException_thenHandleException() throws InterruptedException {
        // Given
        String resourceKey = "resource123";
        Runnable runnable = mock(Runnable.class);
        when(redissonClient.getLock("resource-lock:" + resourceKey)).thenReturn(rLock);
        when(rLock.tryLock()).thenReturn(true); // Simula que se puede obtener el bloqueo
        doThrow(new RuntimeException("Error en el runnable")).when(runnable).run(); // Simula una excepción en el runnable

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            lockResourceService.executeWithLock(resourceKey, runnable);
        });

        // Verifica que se ha lanzado la excepción correcta
        assertEquals("Error en el runnable", exception.getMessage());

        verify(rLock).tryLock(); // Verifica que se haya intentado adquirir el bloqueo
        verify(runnable).run(); // Verifica que se haya ejecutado el runnable
    }
}

