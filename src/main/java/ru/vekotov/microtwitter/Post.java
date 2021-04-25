package ru.vekotov.microtwitter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "posts")
public class Post {
    @Id
    public String id;
    public String author;
    public String text;

    public Post(String author, String text) {
        this.author = author;
        this.text = text;
    }
}
