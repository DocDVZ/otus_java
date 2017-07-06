package ru.otus.L09;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.L09.examples.SimpleEntity;
import ru.otus.L09.frontend.Frontend;
import ru.otus.L09.orm.OrmConfiguration;
import ru.otus.L09.orm.OrmTool;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Timer;

/**
 * Created by dzvyagin on 15.06.2017.
 */
public class App {

    private static final String URL = "jdbc:mysql://localhost/test_schema?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String USER = "admin";
    private static final String PASSWORD = "";
    private static final Integer POOL_SIZE = 3;
    private static final Integer PORT = 8090;

    private static final Logger LOG = LoggerFactory.getLogger(App.class);


    public static void main(String[] args) throws Exception {

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

        // Init Jetty
        LOG.info("Initializing Jetty");
        Thread thread = new Thread(() -> {
            try {
                Frontend frontend = new Frontend();
                frontend.start(PORT);
            } catch (Exception e) {
                LOG.error("Cannot initialize frontend", e);
            }
        });
        thread.start();

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


}
