package com.consulner.app.api;

import com.consulner.app.errors.ApplicationExceptions;
import com.consulner.app.errors.ExceptionHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Function;

public class PostHandler<RequestBody, ResponseBody> extends Handler {

    private Class<RequestBody> requestBodyClass;
    private final Function<RequestBody, ResponseBody> function;
    private String contentType;
    private StatusCode statusCode;

    public PostHandler(ObjectMapper objectMapper, ExceptionHandler exceptionHandler, Class<RequestBody> requestBodyClass,
                       Function<RequestBody,ResponseBody> function, String contentType, StatusCode statusCode) {
        super(objectMapper, exceptionHandler);
        this.requestBodyClass = requestBodyClass;
        this.function = function;
        this.contentType = contentType;
        this.statusCode = statusCode;
    }
    @Override
    protected void execute(HttpExchange exchange) throws IOException {
        byte[] response;
        if ("POST".equals(exchange.getRequestMethod())) {
            ResponseBody responseValue = function.apply(readRequest(exchange.getRequestBody(), requestBodyClass));
            ResponseEntity<ResponseBody> e = new ResponseEntity<>(responseValue,
                    getHeaders(Constants.CONTENT_TYPE, contentType), statusCode);

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

}
