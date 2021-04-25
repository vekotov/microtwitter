package ru.vekotov.microtwitter;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TokenRepository extends MongoRepository<Token, String> {
    @Query("{token:'?0'}")
    Token findTokenByToken(String token);
    void deleteByToken(String token);
}
