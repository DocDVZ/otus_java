package ru.otus.L09;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.L09.frontend.servlets.AjaxServlet;
import ru.otus.L09.frontend.servlets.FrontendServlet;

import javax.servlet.Servlet;

/**
 * Created by DocDVZ on 14.07.2017.
 */
public class FrontMain {

    public static void main(String[] args) throws Exception{
        Server server = new Server(8090);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        Servlet frontend = new FrontendServlet();
        Servlet ajax = new AjaxServlet();
        context.addServlet(new ServletHolder(frontend), "/");
        context.addServlet(new ServletHolder(ajax), "/ajax");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("webapps");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, context});
        server.setHandler(handlers);

        server.start();
        server.join();
    }

}
