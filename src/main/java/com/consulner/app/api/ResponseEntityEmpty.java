package com.consulner.app.api;

import com.sun.net.httpserver.Headers;

public class ResponseEntityEmpty extends ResponseEntityBase {


    public ResponseEntityEmpty(Headers pHeaders, StatusCode pStatusCode) {
        super(pHeaders, pStatusCode);
    }

}