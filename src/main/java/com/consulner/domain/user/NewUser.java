package com.consulner.domain.user;


public class NewUser {

    private final String login;
    private final String password;
    public NewUser(String login,String password){
        this.login = login;
        this.password = password;
    }
    public String getLogin(){ return login; }
    public String getPassword(){ return password; }
    public static NewUserBuilder builder() {
        return new NewUserBuilder();
    }
    public static class NewUserBuilder{

        private String login;
        private String password;

        public NewUserBuilder login(String login) {
            this.login=login;
            return this;
        }

        public NewUserBuilder password(String password) {
            this.password=password;
            return this;
        }

        public NewUser build() {
            return new NewUser(login, password);
        }

    }
}
