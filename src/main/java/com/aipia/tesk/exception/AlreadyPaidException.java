package com.aipia.tesk.exception;

public class AlreadyPaidException extends RuntimeException {

    public AlreadyPaidException(String message) {
        super(message);
    }

    public AlreadyPaidException(String message, Throwable cause) {
        super(message, cause);
    }
}