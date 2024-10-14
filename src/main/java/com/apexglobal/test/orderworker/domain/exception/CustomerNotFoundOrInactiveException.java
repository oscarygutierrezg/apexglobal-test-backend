package com.apexglobal.test.orderworker.domain.exception;

public class CustomerNotFoundOrInactiveException extends RuntimeException {

    public CustomerNotFoundOrInactiveException(String message) {
        super(message);
    }
}