package com.tanveer.security;

public class Login {
    private String userName;
    private String password;
    private static Login login = new Login();

    private Login(){
    }

    public static Login getLogin() {
        return login;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
