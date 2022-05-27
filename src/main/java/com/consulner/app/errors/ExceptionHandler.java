package com.consulner.app.errors;

import com.sun.net.httpserver.HttpExchange;

public interface ExceptionHandler {
    void handle(Throwable throwable, HttpExchange exchange);
}
