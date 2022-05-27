package com.consulner.errors;

import com.consulner.app.Configuration;
import com.consulner.app.api.ErrorResponse;
import com.consulner.app.errors.GlobalExceptionHandler;
import com.consulner.app.errors.InvalidRequestException;
import com.consulner.app.errors.MethodNotAllowedException;
import com.consulner.app.errors.ResourceNotFoundException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ErrorTest {
    Configuration c = new Configuration();
    GlobalExceptionHandler handler=new GlobalExceptionHandler(c.getObjectMapper());
    @Test
    public void canInterpretInvalidRequest() {
        ErrorResponse r = handler.getErrorResponse(new InvalidRequestException("test"));

        assertEquals(400, r.getCode());
        assertEquals("test", r.getMessage());
    }
    @Test
    public void canInterpretMethodNotAllowed() {
        ErrorResponse r = handler.getErrorResponse(new MethodNotAllowedException("test"));

        assertEquals(405, r.getCode());
        assertEquals("test", r.getMessage());
    }
    @Test
    public void canInterpretResourceNotFound() {
        ErrorResponse r = handler.getErrorResponse(new ResourceNotFoundException("test"));

        assertEquals(404, r.getCode());
        assertEquals("test", r.getMessage());
    }
}
