package com.apexglobal.test.orderworker.application.exception;

public class AlreadyBeenProcessedException extends RuntimeException {

    public AlreadyBeenProcessedException(String message) {
        super(message);
    }
}