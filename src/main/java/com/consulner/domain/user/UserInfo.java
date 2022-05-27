package com.consulner.domain.user;

public class UserInfo {
    private final String id;
    private final String login;

    public UserInfo(String id, String login) {
        this.id = id;
        this.login = login;
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }
}
