package com.consulner.app.errors;

public class ApplicationException extends RuntimeException {

    private final int code;

    public ApplicationException(int code, String message) {
        super(message);
        this.code = code;
    }
    public int getCode() {
        return code;
    }
}