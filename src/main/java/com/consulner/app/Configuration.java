package com.consulner.app;

import com.consulner.app.api.ObjectMapper;
import com.consulner.app.errors.ExceptionHandler;
import com.consulner.app.errors.GlobalExceptionHandler;
import com.consulner.data.user.InMemoryUserRepository;
import com.consulner.domain.user.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

public class Configuration {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(){
        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        @Override
        public byte[] writeValueAsBytes(Object pO) throws IOException {
            return objectMapper.writeValueAsBytes(pO);
        }

        @Override
        public <T> T readValue(InputStream pIs, Class<T> pType) throws IOException {
            return objectMapper.readValue(pIs, pType);
        }
        @Override
        public <T> T readValue(Reader r, Class<T> pType) throws IOException {
            return objectMapper.readValue(r, pType);
        }

        @Override
        public <T> List<T> readValues(Reader r, Class<T> pType) throws IOException {
            return objectMapper.readValue(r, new TypeReference<List<T>>() {});
        }
    };
    private final UserRepository USER_REPOSITORY = new InMemoryUserRepository();
    private final ExceptionHandler GLOBAL_ERROR_HANDLER = new GlobalExceptionHandler(OBJECT_MAPPER);

    public ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }


    public UserRepository getUserRepository() {
        return USER_REPOSITORY;
    }

    public ExceptionHandler getErrorHandler() {
        return GLOBAL_ERROR_HANDLER;
    }
}
