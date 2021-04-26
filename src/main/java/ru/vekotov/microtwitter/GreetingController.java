package ru.vekotov.microtwitter;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static ru.vekotov.microtwitter.MicrotwitterApplication.*;

@Controller
@EnableMongoRepositories
@EntityScan
public class GreetingController {
    @Autowired
    public UserRepository userRepository;

    @Autowired
    public PostRepository postRepository;

    @Autowired
    public TokenRepository tokenRepository;

    @GetMapping("/")
    public String index(@CookieValue(value = "token", defaultValue = "null") String token,
                        Model model) {
        model.addAttribute("postList", postRepository.findAll());
        Token userToken = tokenRepository.findTokenByToken(token);
        if(userToken != null) model.addAttribute("status", "yes_login");
        else model.addAttribute("status","no_login");
        return "index";
    }

    @PostMapping("/create_post")
    public String createPost(
            @RequestParam(name="text") String text,
            @CookieValue(value = "token", defaultValue = "null") String token) {
        if(text
                .replace(" ", "")
                .replace("\t", "")
                .replace("\n", "")
                .equals("")) return "redirect:/";
        Token userToken = tokenRepository.findTokenByToken(token);
        if(userToken == null) {
            postRepository.save(new Post("Anonymous", text));
        } else {
            postRepository.save(new Post(userToken.login, text));
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(@CookieValue(value = "token", defaultValue = "null") String token,
                        Model model) {
        Token userToken = tokenRepository.findTokenByToken(token);
        if(userToken != null) return "redirect:/";
        model.addAttribute("status","no_login");
        return "login";
    }

    @PostMapping("/login")
    public String postLogin(
            @RequestParam(name="login") String login,
            @RequestParam(name="password") String password,
            HttpServletResponse response) {
        if(checkPassword(login, password)) return "redirect:/login";
        addToken(login, response);
        return "redirect:/";
    }

    @PostMapping("/register")
    public String postRegister(
            @RequestParam(name="login", required=true) String login,
            @RequestParam(name="password", required=true) String password,
            @RequestParam(name="password_again", required = true) String passwordAgain,
            HttpServletResponse response) {
        if(!password.equals(passwordAgain)) return "redirect:/login";
        User findUser = userRepository.findUserByLogin(login);
        if(findUser != null) return "redirect:/login";
        User user = new User(login, hashPassword(password));
        userRepository.save(user);
        addToken(login, response);
        return "redirect:/";
    }

    @GetMapping("/unlogin")
    public String unlogin(@CookieValue(value = "token", defaultValue = "null") String token) {
        tokenRepository.deleteByToken(token);
        return "redirect:/";
    }

    public void addToken(String login, HttpServletResponse response) {
        String token = UUID.randomUUID().toString();
        response.addCookie(createCookie(token));
        tokenRepository.save(new Token(token, login));
    }

    public Cookie createCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setMaxAge(-1);
        return cookie;
    }

    public boolean checkPassword(String login, String password) {
        User findUser = userRepository.findUserByLogin(login);
        if(findUser == null) return false;
        return findUser.password.equals(hashPassword(password));
    }

    public static String hashPassword(String password) {
            return DigestUtils.sha256Hex(password);
    }
}