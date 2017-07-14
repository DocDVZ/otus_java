package ru.otus.L09.frontend;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DocDVZ on 14.07.2017.
 */
public class FrontendHandler extends AbstractHandler {


    @Override
    public void handle(String s, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        Map<String, Object> pageVariables = new HashMap<>();
//        pageVariables.put("refreshPeriod", "1000");
//        pageVariables.put("serverTime", getTime());

        response.getWriter().println(PageGenerator.instance().getPage("monitoring.html", pageVariables));
    }
}
