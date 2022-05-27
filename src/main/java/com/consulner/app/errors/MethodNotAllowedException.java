package com.consulner.app.errors;

import com.consulner.app.api.StatusCode;

class MethodNotAllowedException extends ApplicationException {

    MethodNotAllowedException(String message) {
        super(StatusCode.METHOD_NOT_ALLOWED.getCode(), message);
    }
}
