package com.consulner.app.errors;

import com.consulner.app.api.StatusCode;

class InvalidRequestException extends ApplicationException {

    public InvalidRequestException(String message) {
        super(StatusCode.BAD_REQUEST.getCode(), message);
    }
}
