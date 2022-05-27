package com.consulner.app.api;

import com.consulner.app.errors.ApplicationExceptions;
import com.consulner.app.errors.ExceptionHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public abstract class PostHandler<RequestBody, ResponseBody> extends Handler {

    private Class<RequestBody> requestBodyClass;

    public PostHandler(ObjectMapper objectMapper, ExceptionHandler exceptionHandler, Class<RequestBody> requestBodyClass) {
        super(objectMapper, exceptionHandler);
        this.requestBodyClass = requestBodyClass;
    }
    @Override
    protected void execute(HttpExchange exchange) throws IOException {
        byte[] response;
        if ("POST".equals(exchange.getRequestMethod())) {
            ResponseBody responseValue = doPost(readRequest(exchange.getRequestBody(), requestBodyClass));
            ResponseEntity<ResponseBody> e = new ResponseEntity<>(responseValue,
                    getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);

            exchange.getResponseHeaders().putAll(e.getHeaders());
            exchange.sendResponseHeaders(e.getStatusCode().getCode(), 0);
            response = super.writeResponse(e.getBody());
        } else {
            throw ApplicationExceptions.methodNotAllowed(
                    "Method " + exchange.getRequestMethod() + " is not allowed for " + exchange.getRequestURI()).get();
        }

        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }

    protected abstract ResponseBody doPost(RequestBody requestBody);
}
