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
 * Created by dzvyagin on 18.07.2017.
 */
public class OrmServlet extends HttpServlet {


    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();
        String page = PageGenerator.instance().getPage("orm.html", pageVariables);
        response.getWriter().println(page);
    }


}
