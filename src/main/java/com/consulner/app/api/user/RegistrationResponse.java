package com.consulner.app.api.user;

public class RegistrationResponse {

    private final String id;

    public RegistrationResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}