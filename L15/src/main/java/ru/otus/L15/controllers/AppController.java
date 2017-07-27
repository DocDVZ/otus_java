package ru.otus.L15.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class AppController {



    @RequestMapping(value = {"/home", "/"})
    public String home() {
        System.out.println("HomeController: Passing through...");
        return "redirect:login";
    }

    @RequestMapping(value = {"/login"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String login(@RequestParam String login, HttpServletRequest request, HttpServletResponse response) {
        response.addCookie(new Cookie("L15.1-login", login));
        request.getSession().setAttribute("login", login);

        System.out.println("HomeController: Passing through...");
        return "index";
    }



}
