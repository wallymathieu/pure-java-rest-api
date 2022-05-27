package com.consulner.app.errors;

import com.consulner.app.api.StatusCode;

public class InvalidRequestException extends ApplicationException {

    public InvalidRequestException(String message) {
        super(StatusCode.BAD_REQUEST.getCode(), message);
    }
}
