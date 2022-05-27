package com.consulner.app.errors;

import com.consulner.app.api.StatusCode;

public class ResourceNotFoundException extends ApplicationException {

    public ResourceNotFoundException(String message) {
        super(StatusCode.NOT_FOUND.getCode(), message);
    }
}
