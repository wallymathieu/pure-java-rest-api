package com.consulner.app;

import com.consulner.app.api.ObjectMapper;
import com.consulner.app.errors.GlobalExceptionHandler;
import com.consulner.data.user.InMemoryUserRepository;
import com.consulner.domain.user.UserRepository;
import com.consulner.domain.user.UserService;

import java.io.IOException;
import java.io.InputStream;

class Configuration {

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
    private static final UserRepository USER_REPOSITORY = new InMemoryUserRepository();
    private static final UserService USER_SERVICE = new UserService(USER_REPOSITORY);
    private static final GlobalExceptionHandler GLOBAL_ERROR_HANDLER = new GlobalExceptionHandler(OBJECT_MAPPER);

    static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    static UserService getUserService() {
        return USER_SERVICE;
    }

    static UserRepository getUserRepository() {
        return USER_REPOSITORY;
    }

    public static GlobalExceptionHandler getErrorHandler() {
        return GLOBAL_ERROR_HANDLER;
    }
}
