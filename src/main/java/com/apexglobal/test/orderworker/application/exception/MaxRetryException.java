package com.apexglobal.test.orderworker.application.exception;

public class MaxRetryException extends RuntimeException {

    public MaxRetryException(String message) {
        super(message);
    }
}