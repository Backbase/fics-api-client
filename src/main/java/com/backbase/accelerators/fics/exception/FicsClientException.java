package com.backbase.accelerators.fics.exception;

public class FicsClientException extends RuntimeException {

    public FicsClientException(String message) {
        super(message);
    }

    public FicsClientException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
