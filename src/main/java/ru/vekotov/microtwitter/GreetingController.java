package ru.vekotov.microtwitter;

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
public class GreetingController {

    @GetMapping("/")
    public String index(@CookieValue(value = "token", defaultValue = "null") String token,
                        Model model) {
        model.addAttribute("postList", postList);
        if(tokens.containsKey(token)) model.addAttribute("status", "yes_login");
        else model.addAttribute("status","no_login");
        return "index";
    }

    @PostMapping("/create_post")
    public String createPost(
            @RequestParam(name="text") String text,
            @CookieValue(value = "token", defaultValue = "null") String token) {
        if(!tokens.containsKey(token)) postList.addFirst(new Post("Anonymous", text));
        else postList.addFirst(new Post(tokens.get(token).login, text));
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(@CookieValue(value = "token", defaultValue = "null") String token,
                        Model model) {
        if(tokens.containsKey(token)) return "redirect:/";
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
        if(userList.containsKey(login)) return "redirect:/login";
        if(!password.equals(passwordAgain)) return "redirect:/login";

        userList.put(login, new User(login, password));
        addToken(login, response);
        return "redirect:/";
    }

    @GetMapping("/unlogin")
    public String unlogin(@CookieValue(value = "token", defaultValue = "null") String token) {
        tokens.remove(token);
        return "redirect:/";
    }

    public static void addToken(String login, HttpServletResponse response) {
        String token = UUID.randomUUID().toString();
        response.addCookie(createCookie(token));
        tokens.put(token, userList.get(login));
    }

    public static Cookie createCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setMaxAge(-1);
        return cookie;
    }

    public static boolean checkPassword(String login, String password) {
        return !userList.containsKey(login) || !userList.get(login).password.equals(password);
    }
}