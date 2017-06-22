package ru.otus.L09.orm;

import net.sf.ehcache.CacheManager;
import ru.otus.L09.orm.exceptions.ConnectionException;
import ru.otus.L09.orm.exceptions.NotImplementedException;
import ru.otus.L09.orm.exceptions.ValidationException;
import ru.otus.L09.orm.metadata.ColumnMetadata;
import ru.otus.L09.orm.metadata.TableMetadata;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by dzvyagin on 15.06.2017.
 */
public class OrmSessionFactory implements EntityManagerFactory {

    private OrmConfiguration configuration;
    private volatile boolean isOpen = false;
    private Set<TableMetadata> tableMetadatas;
    private CacheManager cacheManager;
    private static AtomicLong entityManagerCounter = new AtomicLong(0);

    OrmSessionFactory(OrmConfiguration configuration){
        this.configuration = configuration;
        try {
            Class.forName(configuration.getJdbcDriver());
            isOpen = true;
            cacheManager = CacheManager.getInstance();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
            throw new ConnectionException(e);
        }
    }

    private Connection getConnection() throws SQLException{
        return DriverManager.getConnection(configuration.getDbUrl(), configuration.getUser(), configuration.getPassword());
    }

    @Override
    public EntityManager createEntityManager() {
        Connection connection;
        try {
            connection = getConnection();
        } catch (SQLException e){
            e.printStackTrace();
            throw new ConnectionException(e);
        }
        EntityManager entityManager = new OrmSession(connection, cacheManager);
        return entityManager;
    }

    @Override
    public EntityManager createEntityManager(Map map) {
        throw new NotImplementedException();
    }

    @Override
    public void close() {
//        try {

            isOpen = false;

//        } catch (SQLException e){
//            e.printStackTrace();
//            throw new ConnectionException(e);
//        }
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }


    public Set<TableMetadata> getTableMetadatas() {
        return tableMetadatas;
    }

    public void setTableMetadatas(Set<TableMetadata> tableMetadata) {
        this.tableMetadatas = tableMetadata;
    }

    void validateTables() {
        for (TableMetadata tableMetadata : tableMetadatas){
            validateTable(tableMetadata);
        }
    }

    void validateTable(TableMetadata metadata){
        try( Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS `" + metadata.getName() + "` (\n";
            for (ColumnMetadata columnMetadata : metadata.getColumns()) {
                sql += "`" + columnMetadata.getName() + "` " + columnMetadata.getType().getMysqlType() + columnMetadata.getTypeSize() + ",\n" ;
            }
            sql += "primary key(`" + metadata.getPrimaryKeyField().getName() + "`)";
            sql += ");";
            System.out.println("Executing sql: " + sql);
            statement.execute(sql);

        } catch (SQLException e){
            e.printStackTrace();
            throw new ValidationException(e);
        }
    }

    static AtomicLong getEntityManagerCounter() {
        return entityManagerCounter;
    }
}
