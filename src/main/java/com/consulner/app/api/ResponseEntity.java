package com.consulner.app.api;

import com.sun.net.httpserver.Headers;

public class ResponseEntity<T> extends Response {

    private final T body;

    public ResponseEntity(T pBody, Headers pHeaders, StatusCode pStatusCode) {
        super(pHeaders,pStatusCode);
        body = pBody;
    }

    public T getBody() {
        return body;
    }

}
