package ru.vekotov.microtwitter;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> { // Long: Type of Employee ID.
    @Query("{login:'?0'}")
    User findUserByLogin(String login);
}