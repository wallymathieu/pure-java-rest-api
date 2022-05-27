package com.consulner.app.api;

import com.sun.net.httpserver.Headers;

public class EmptyResponse extends Response {


    public EmptyResponse(Headers pHeaders, StatusCode pStatusCode) {
        super(pHeaders, pStatusCode);
    }

}