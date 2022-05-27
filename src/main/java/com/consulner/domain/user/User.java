package com.consulner.domain.user;

public class User {

    private final String id;
    private final String login;
    private final String password;

    public User(String id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
    public static class UserBuilder{

        private String login;
        private String password;
        private String id;

        public UserBuilder login(String login) {
            this.login=login;
            return this;
        }

        public UserBuilder password(String password) {
            this.password=password;
            return this;
        }

        public UserBuilder id(String id) {
            this.id = id;
            return this;
        }

        public User build() {
            return new User(id, login, password);
        }
    }
}
