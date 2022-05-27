package com.consulner.app.api;

public class ErrorResponse {

    int code;
    String message;
    
    public ErrorResponse(int pCode, String pMessage) {
        code = pCode;
        message = pMessage;
    }

    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    
    public static class ErrorResponseBuilder {
        private String message;
        private int code;

        public ErrorResponseBuilder message(String pMessage) {
                this.message=pMessage;
                return this;
        }

        public ErrorResponseBuilder code(int pCode) {
                this.code = pCode;
                return this;
        }

        public ErrorResponse build() {
                return new ErrorResponse(code, message);
        }
    }

}
