package ru.vekotov.microtwitter;

public class Token {
    String token;
    String login;

    public Token(String token, String login) {
        this.token = token;
        this.login = login;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}
