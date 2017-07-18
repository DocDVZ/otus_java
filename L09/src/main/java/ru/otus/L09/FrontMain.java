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
import ru.otus.L09.frontend.servlets.AjaxServlet;
import ru.otus.L09.frontend.servlets.MonitoringServlet;
import ru.otus.L09.frontend.servlets.LoginServlet;
import ru.otus.L09.orm.OrmConfiguration;
import ru.otus.L09.orm.OrmTool;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
    private static final String PASSWORD = "admin";
    private static final Integer POOL_SIZE = 3;
    private static final Integer PORT = 8090;

    private static final Logger LOG = LoggerFactory.getLogger(FrontMain.class);


    public static void startORM() throws Exception {

        LOG.info("Starting application");
        OrmConfiguration configuration = new OrmConfiguration();
        configuration.setDbUrl(URL);
        configuration.setJdbcDriver(DRIVER);
        configuration.setUser(USER);
        configuration.setPassword(PASSWORD);
        configuration.setPoolSize(POOL_SIZE);

        // Init ORM
        LOG.info("Initializing ORM");
        OrmTool ormTool = OrmTool.getInstance();
        ormTool.init(configuration);

        LOG.info("Do some work");
        EntityManagerFactory emf = ormTool.getSessionFactory();
        EntityManager em = emf.createEntityManager();

        SimpleEntity se = new SimpleEntity();
        se.setIntField(1);
        se.setBigDecimalField(new BigDecimal(2));
        se.setBigIntegerField(BigInteger.valueOf(3));
        se.setBoolField(false);
        se.setDateField(new Date());
        se.setStrField("String");
        em.persist(se);

        se.setStrField("String2");
        em.merge(se);
        SimpleEntity se2 = em.find(SimpleEntity.class, 1);
        LOG.info("Selected se2: " + se2);
        em.remove(se);
        em.close();
    }

    public static void main(String[] args) throws Exception{

        startORM();

        Server server = new Server(PORT);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(new LoginServlet("anonymous")), "/login");
        context.addServlet(MonitoringServlet.class, "/monitoring");
        context.addServlet(AjaxServlet.class, "/ajax");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("public");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, context});
        server.setHandler(handlers);

        server.start();
        server.join();
    }

}
