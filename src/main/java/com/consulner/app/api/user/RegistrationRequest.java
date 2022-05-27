package com.consulner.app.api.user;


class RegistrationRequest {

    private final String login;
    private final String password;
    public RegistrationRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }
    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }
}
