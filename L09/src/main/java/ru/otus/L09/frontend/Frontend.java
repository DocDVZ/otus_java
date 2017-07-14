package ru.otus.L09.frontend;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.L09.frontend.servlets.FrontendServlet;

import javax.servlet.Servlet;
import java.net.URI;
import java.net.URL;

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
        context.addServlet(new ServletHolder(frontend), "/monitoring");
//        context.setBaseResource(Resource.newResource("static"));
//        ResourceHandler resource_handler = new ResourceHandler();
//        resource_handler.setDirectoriesListed(true);
//        resource_handler.setResourceBase("static");
//        LOG.warn(resource_handler.getResourceBase());
//        HandlerList handlers = new HandlerList();
//        handlers.setHandlers(new Handler[]{resource_handler, context});
//        server.setHandler(handlers);

        // Figure out what path to serve content from
        ClassLoader cl = Frontend.class.getClassLoader();
        // We look for a file, as ClassLoader.getResource() is not
        // designed to look for directories (we resolve the directory later)
        URL f = cl.getResource("templates/monitoring.html");
        if (f == null)
        {
            throw new RuntimeException("Unable to find resource directory");
        }

        // Resolve file to directory
        URI webRootUri = f.toURI().resolve("./").normalize();
        System.err.println("WebRoot is " + webRootUri);

        ResourceHandler handler = new ResourceHandler();
        handler.setBaseResource(Resource.newResource(webRootUri));
        handler.setDirectoriesListed(true);

        server.setHandler(handler);






        server.setHandler(context);

        server.start();
        server.join();
        System.out.println("Finished");

    }
}
