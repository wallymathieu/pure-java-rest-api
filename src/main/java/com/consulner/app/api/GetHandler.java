package com.consulner.app.api;

import com.consulner.app.errors.ApplicationExceptions;
import com.consulner.app.errors.ExceptionHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Supplier;

public class GetHandler<ResponseBody> extends Handler {
    private final Supplier<ResponseBody> function;
    private String contentType;
    private StatusCode statusCode;

    public GetHandler(ObjectMapper objectMapper, ExceptionHandler exceptionHandler,
                      Supplier<ResponseBody> function, String contentType, StatusCode statusCode) {
        super(objectMapper, exceptionHandler);
        this.function = function;
        this.contentType = contentType;
        this.statusCode = statusCode;
    }

    @Override
    protected void execute(HttpExchange exchange) throws IOException {
        byte[] response;
        if ("GET".equals(exchange.getRequestMethod())) {
            ResponseBody responseValue = function.get();
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
