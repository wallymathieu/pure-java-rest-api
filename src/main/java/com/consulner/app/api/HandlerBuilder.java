package com.consulner.app.api;

import com.consulner.app.errors.ApplicationExceptions;
import com.consulner.app.errors.ExceptionHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.consulner.app.api.ApiUtils.splitQuery;
import static com.consulner.app.api.Handler.getHeaders;

public class HandlerBuilder {
    private final ObjectMapper objectMapper;
    private final ExceptionHandler exceptionHandler;
    private final String defaultContentType;


    public HandlerBuilder(ObjectMapper objectMapper, ExceptionHandler exceptionHandler) {
        this(objectMapper, exceptionHandler, null);
    }

    public HandlerBuilder(ObjectMapper objectMapper, ExceptionHandler exceptionHandler, String contentType) {
        this.objectMapper = objectMapper;
        this.exceptionHandler = exceptionHandler;
        this.defaultContentType = contentType == null ? Constants.APPLICATION_JSON: contentType;
    }

    public PostHandlerBuilder post() {
        return new PostHandlerBuilder();
    }
    public GetHandlerBuilder get() {
        return new GetHandlerBuilder();
    }
    public HandlerBuilder setDefaultContentType(String defaultContentType) {
        return new HandlerBuilder(objectMapper, exceptionHandler, defaultContentType);
    }

    public class PostHandlerBuilder {
        public <RequestBody, ResponseBody> PostHandler<RequestBody, ResponseBody> okBody(
                Function<RequestEntity<RequestBody>, ResponseEntity<ResponseBody>> function, Class<RequestBody> requestBodyClass){
            return new PostHandler<>(objectMapper,exceptionHandler, requestBodyClass, function);
        }
        public <RequestBody, ResponseBody> PostHandler<RequestBody, ResponseBody> okBodyContent(
                Function<RequestBody, ResponseBody> function, Class<RequestBody> requestBodyClass){
            return okBody((r)->new ResponseEntity<>(function.apply(r.getRequestBody()),
                                getHeaders(Constants.CONTENT_TYPE, defaultContentType), StatusCode.OK), requestBodyClass);
        }
    }
    public class GetHandlerBuilder {
        public <ResponseBody> GetHandler<ResponseBody> ok(
                Function<Request, ResponseEntity<ResponseBody>> function){
            return new GetHandler<>(objectMapper, exceptionHandler, function);
        }
        public <ResponseBody> GetHandler<ResponseBody> okSupplier(
                Supplier<ResponseBody> function){
            return ok((r)-> new ResponseEntity<>(function.get(),
                    getHeaders(Constants.CONTENT_TYPE, defaultContentType), StatusCode.OK));
        }
    }
    private static class PostHandler<RequestBody, ResponseBody> extends Handler {

        private final Class<RequestBody> requestBodyClass;
        private final Function<RequestEntity<RequestBody>, ResponseEntity<ResponseBody>> function;

        public PostHandler(ObjectMapper objectMapper, ExceptionHandler exceptionHandler, Class<RequestBody> requestBodyClass,
                           Function<RequestEntity<RequestBody>, ResponseEntity<ResponseBody>> function) {
            super(objectMapper, exceptionHandler);
            this.requestBodyClass = requestBodyClass;
            this.function = function;
        }

        @Override
        protected void execute(HttpExchange exchange) throws IOException {
            byte[] response;
            if ("POST".equals(exchange.getRequestMethod())) {
                ResponseEntity<ResponseBody> e = function.apply(
                        new RequestEntity<>(readRequest(exchange.getRequestBody(), requestBodyClass),
                            splitQuery(exchange.getRequestURI().getRawQuery())));

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

    static class GetHandler<ResponseBody> extends Handler {
        private final Function<Request,ResponseEntity<ResponseBody>> function;

        public GetHandler(ObjectMapper objectMapper, ExceptionHandler exceptionHandler,
                          Function<Request,ResponseEntity<ResponseBody>> function) {
            super(objectMapper, exceptionHandler);
            this.function = function;
        }

        @Override
        protected void execute(HttpExchange exchange) throws IOException {
            byte[] response;
            if ("GET".equals(exchange.getRequestMethod())) {
                ResponseEntity<ResponseBody> e = function.apply(new EmptyRequest(splitQuery(exchange.getRequestURI().getRawQuery())));

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

}
