package ru.otus.L09.frontend;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;

/**
 * Created by DocDVZ on 03.07.2017.
 */
public class Frontend {

    private static final Logger LOG = LoggerFactory.getLogger(Frontend.class);

    public void start(int port) throws Exception{

        if (port<1 || port>65535){
            LOG.error("WRONG PORT NUMBER " + port);
            throw new IllegalArgumentException("Port should be between 1 and 65545");
        }

        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        Servlet frontend = new FrontendServlet();
        context.addServlet(new ServletHolder(frontend), "/*");
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("src/main/resources");
        LOG.warn(resource_handler.getResourceBase());
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});
        server.setHandler(handlers);
        server.setHandler(context);

        server.start();
        server.join();
        System.out.println("Finished");

    }
}
