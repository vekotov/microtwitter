package ru.vekotov.microtwitter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tokens")
public class Token {
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

    public Token(String token, String login) {
        this.token = token;
        this.login = login;
    }

    @Id
    @Indexed(unique = true)
    String token;
    String login;
}
