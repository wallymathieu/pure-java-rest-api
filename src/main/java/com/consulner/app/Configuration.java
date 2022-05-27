package com.consulner.app;

import com.consulner.app.api.ObjectMapper;
import com.consulner.app.errors.ExceptionHandler;
import com.consulner.app.errors.GlobalExceptionHandler;
import com.consulner.data.user.InMemoryUserRepository;
import com.consulner.domain.user.UserRepository;
import com.consulner.domain.user.UserService;

import java.io.IOException;
import java.io.InputStream;

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

    };
    private final UserRepository USER_REPOSITORY = new InMemoryUserRepository();
    private final UserService USER_SERVICE = new UserService(USER_REPOSITORY);
    private final ExceptionHandler GLOBAL_ERROR_HANDLER = new GlobalExceptionHandler(OBJECT_MAPPER);

    public ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public UserService getUserService() {
        return USER_SERVICE;
    }

    UserRepository getUserRepository() {
        return USER_REPOSITORY;
    }

    public ExceptionHandler getErrorHandler() {
        return GLOBAL_ERROR_HANDLER;
    }
}
