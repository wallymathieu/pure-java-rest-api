package com.consulner.app.api.user;

class RegistrationResponse {

    private final String id;

    RegistrationResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}