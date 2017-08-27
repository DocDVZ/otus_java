package ru.otus.L161.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ViewsController {

    private static final Logger LOG = LoggerFactory.getLogger(ViewsController.class);


    @RequestMapping(value = {"/home", "/"})
    public String home() {
        LOG.debug("HomeController: / or /home, redirecting to /login");
        return "redirect:login";
    }

    @RequestMapping(value = {"/login"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String loginPage(@RequestParam(required = false, name = "login", defaultValue = "anonymous") String login, HttpServletRequest request, HttpServletResponse response, Model model) {
        LOG.debug("HomeController: /login, login={}", login);
        response.addCookie(new Cookie("L161.1-login", login));
        request.getSession().setAttribute("login", login);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=utf-8");
        model.addAttribute("login", login);
        return "loginPage";
    }

    @RequestMapping(value = "/monitoring")
    public String getMonitoring(){
        return "monitoring";
    }

    @RequestMapping(value = "/orm")
    public String getORM(){
        return "orm";
    }



}
