package ru.otus.L09;

import ru.otus.L09.examples.SimpleEntity;
import ru.otus.L09.orm.OrmConfiguration;
import ru.otus.L09.orm.OrmTool;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by dzvyagin on 15.06.2017.
 */
public class App {

    private static final String URL = "jdbc:mysql://localhost/test_schema?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String USER = "admin";
    private static final String PASSWORD = "";


    public static void main(String[] args) throws Exception{
        
        OrmConfiguration configuration = new OrmConfiguration();
        configuration.setDbUrl(URL);
        configuration.setJdbcDriver(DRIVER);
        configuration.setUser(USER);
        configuration.setPassword(PASSWORD);

        OrmTool ormTool = OrmTool.getInstance();
        ormTool.init(configuration);
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
        System.out.println("Selected se2: " + se2);
        em.remove(se);



    }


}
