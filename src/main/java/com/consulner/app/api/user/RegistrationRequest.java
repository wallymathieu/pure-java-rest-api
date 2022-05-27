package com.consulner.app.api.user;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RegistrationRequest {

    private final String login;
    private final String password;
    @JsonCreator
    public RegistrationRequest(@JsonProperty("login") String login,@JsonProperty("password") String password) {
        this.login = login;
        this.password = password;
    }
    @JsonProperty("login")
    public String getLogin() {
        return login;
    }
    @JsonProperty("password")
    public String getPassword() {
        return password;
    }
}
