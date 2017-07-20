package ru.otus.L09;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.L09.examples.SimpleEntity;
import ru.otus.L09.frontend.Frontend;
import ru.otus.L09.frontend.servlets.AjaxServlet;
import ru.otus.L09.frontend.servlets.FrontendServlet;
import ru.otus.L09.orm.OrmConfiguration;
import ru.otus.L09.orm.OrmTool;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.Servlet;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by DocDVZ on 14.07.2017.
 */
public class FrontMain {

    private static final String URL = "jdbc:mysql://localhost/test_schema?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String USER = "admin";
    private static final String PASSWORD = "";
    private static final Integer POOL_SIZE = 3;
    private static final Integer PORT = 8090;

    private static final Logger LOG = LoggerFactory.getLogger(App.class);


    public static void main(String[] args) throws Exception{

        Server server = new Server(PORT);
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
