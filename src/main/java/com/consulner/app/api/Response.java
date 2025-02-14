package com.consulner.app.api;

import com.sun.net.httpserver.Headers;

public abstract class Response {

    private final Headers headers;
    private final StatusCode statusCode;

    public Response(Headers pHeaders, StatusCode pStatusCode) {
        headers = pHeaders;
        statusCode = pStatusCode;
    }

    public Headers getHeaders() {
        return headers;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }
}