package com.consulner.app.errors;

import com.consulner.app.api.StatusCode;

class ResourceNotFoundException extends ApplicationException {

    ResourceNotFoundException(String message) {
        super(StatusCode.NOT_FOUND.getCode(), message);
    }
}
