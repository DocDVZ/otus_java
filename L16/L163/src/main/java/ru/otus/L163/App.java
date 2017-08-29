package ru.otus.L163;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.L162.channel.MessageChannel;
import ru.otus.L162.channel.SocketDaoMessageChannel;
import ru.otus.L162.messaging.messages.DaoSocketMessage;
import ru.otus.L163.messaging.DaoRequestHandler;
import ru.otus.L163.messaging.DaoRequestProcessor;
import ru.otus.L163.messaging.RequestProcessor;
import ru.otus.L163.orm.OrmSessionFactory;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Created by DocDVZ on 29.08.2017.
 */
public class App {

    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String JDBC_URL = "jdbc:mysql://localhost/test_schema?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = null;
    private static final int MAXIMUM_POOL_SIZE = 5;
    private static final Logger LOG = LoggerFactory.getLogger(App.class);



    public static void main(String[] args) throws  Exception{

        LOG.info("Initializing db");
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setPoolName("HikariCP");
        dataSource.setConnectionTestQuery("SELECT 1");
        dataSource.setDriverClassName(DRIVER_NAME);
        dataSource.setJdbcUrl(JDBC_URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setAutoCommit(false);
        dataSource.setMaximumPoolSize(MAXIMUM_POOL_SIZE);
        dataSource.setIdleTimeout(60);

        LOG.info("Initializing service connections");
        String host = null;
        Integer port = null;
        if (args.length == 2){
            host = args[0];
            port = Integer.parseInt(args[1]);
        } else if (args.length == 1){
            LOG.info("1 parameter, default host");
            host = "localhost";
            port = Integer.parseInt(args[0]);
        } else {
            LOG.info("No parameters, default host:port");
            host = "localhost";
            port = 5050;
        }

        LOG.info("Trying to connect message service at " + host + ":" + port);
        EntityManagerFactory emf = new OrmSessionFactory(dataSource, "ru.otus.L162.model");
        RequestProcessor<DaoSocketMessage> processor = new DaoRequestProcessor(emf);
        MessageChannel<DaoSocketMessage> channel = new SocketDaoMessageChannel(host, port);
        DaoRequestHandler requestHandler = new DaoRequestHandler(processor, channel);
        requestHandler.start();
    }
}
