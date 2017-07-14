package ru.otus.L09.frontend.servlets;

import ru.otus.L09.frontend.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DocDVZ on 03.07.2017.
 */
public class FrontendServlet extends HttpServlet {

    private String login = "";

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();
//        pageVariables.put("lastLogin", login == null ? "" : login);
        String page = PageGenerator.instance().getPage("monitoring.html", pageVariables);
        response.getWriter().println(page);
    }

//    public void doPost(HttpServletRequest request,
//                       HttpServletResponse response) throws ServletException, IOException {
//        login = request.getParameter("login");
//        response.setContentType("text/html;charset=utf-8");
//        response.setStatus(HttpServletResponse.SC_OK);
//        Map<String, Object> pageVariables = new HashMap<>();
//        pageVariables.put("lastLogin", login == null ? "" : login);
//        response.getWriter().println(PageGenerator.instance().getPage("monitoring.html", pageVariables));
//    }
}