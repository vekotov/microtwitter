package ru.vekotov.microtwitter;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
public class MicrotwitterApplication {
	static Logger logger = LoggerFactory.getLogger(MicrotwitterApplication.class);

	public static Deque<Post> postList = new ArrayDeque<>();
	public static Map<String, User> userList = new HashMap<>();
	public static Map<String, User> tokens = new HashMap<>();

	public static MongoClient mongoClient = MongoClients.create(
			"mongodb+srv://admin:<password>@cluster0.feqan.mongodb.net/myFirstDatabase?retryWrites=true&w=majority"
	);

	public static MongoDatabase postsDB = mongoClient.getDatabase("posts");
	public static MongoDatabase usersDB = mongoClient.getDatabase("users");
	public static MongoDatabase tokenDB = mongoClient.getDatabase("tokens");

	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(MicrotwitterApplication.class);
		app.setDefaultProperties(Collections
				.singletonMap("server.port", "80"));
		app.run(args);
	}

}
