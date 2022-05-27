package com.consulner.app.errors;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.consulner.app.api.Constants;
import com.consulner.app.api.ErrorResponse;
import com.consulner.app.api.ErrorResponse.ErrorResponseBuilder;
import com.consulner.app.api.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

public class GlobalExceptionHandler implements ExceptionHandler {
    private static final Logger mLog = Logger.getLogger(GlobalExceptionHandler.class.getName());
    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(Throwable throwable, HttpExchange exchange) {
        try {
            throwable.printStackTrace();
            exchange.getResponseHeaders().set(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
            ErrorResponse response = getErrorResponse(throwable);
            exchange.sendResponseHeaders(response.getCode(), 0);
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(objectMapper.writeValueAsBytes(response.getMessage()));
            responseBody.close();
        } catch (Exception e) {
            mLog.log(Level.SEVERE,"Failed to write exception to response", e);
        }
    }

    public ErrorResponse getErrorResponse(Throwable throwable) {
        ErrorResponseBuilder responseBuilder = ErrorResponse.builder();
        if (throwable instanceof ApplicationException) {
            ApplicationException exc = (ApplicationException) throwable;
            responseBuilder.message(exc.getMessage()).code(exc.getCode());
        } else {
            responseBuilder.message(throwable.getMessage()).code(500);
        }
        return responseBuilder.build();
    }
}
