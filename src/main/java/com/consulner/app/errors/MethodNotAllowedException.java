package com.consulner.app.errors;

import com.consulner.app.api.StatusCode;

public class MethodNotAllowedException extends ApplicationException {

    public MethodNotAllowedException(String message) {
        super(StatusCode.METHOD_NOT_ALLOWED.getCode(), message);
    }
}
