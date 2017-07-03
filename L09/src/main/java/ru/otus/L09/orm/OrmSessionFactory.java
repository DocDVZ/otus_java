package ru.otus.L09.orm;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.sf.ehcache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.L09.orm.exceptions.ConnectionException;
import ru.otus.L09.orm.exceptions.NotImplementedException;
import ru.otus.L09.orm.exceptions.ValidationException;
import ru.otus.L09.orm.metadata.ColumnMetadata;
import ru.otus.L09.orm.metadata.TableMetadata;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.Connection;
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
    private DataSource dataSource;
    private volatile boolean isOpen = false;
    private Set<TableMetadata> tableMetadatas;
    private CacheManager cacheManager;
    private static AtomicLong entityManagerCounter = new AtomicLong(0);


    private static final Logger LOG = LoggerFactory.getLogger(OrmSessionFactory.class);

    OrmSessionFactory(OrmConfiguration configuration){
        this.configuration = configuration;
        try {
            Class.forName(configuration.getJdbcDriver());
            isOpen = true;
            cacheManager = CacheManager.getInstance();
            HikariConfig hikariConfig = getHikariConfig(configuration);
            dataSource = new HikariDataSource(hikariConfig);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ConnectionException(e);
        }
    }

    private HikariConfig getHikariConfig(OrmConfiguration configuration) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(configuration.getDbUrl());
        hikariConfig.setUsername(configuration.getUser());
        hikariConfig.setPassword(configuration.getPassword());
        hikariConfig.setMaximumPoolSize(configuration.getPoolSize());
        hikariConfig.setAutoCommit(false);
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return hikariConfig;
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public EntityManager createEntityManager() {
        Connection connection;
        try {
            connection = getConnection();
        } catch (SQLException e) {
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
        //TODO close all entityManagers
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
        for (TableMetadata tableMetadata : tableMetadatas) {
            validateTable(tableMetadata);
        }
    }

    void validateTable(TableMetadata metadata) {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS `" + metadata.getName() + "` (\n";
            for (ColumnMetadata columnMetadata : metadata.getColumns()) {
                sql += "`" + columnMetadata.getName() + "` " + columnMetadata.getType().getMysqlType() + columnMetadata.getTypeSize() + ",\n";
            }
            sql += "primary key(`" + metadata.getPrimaryKeyField().getName() + "`)";
            sql += ");";

            LOG.debug("Executing sql: " + sql);
            statement.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ValidationException(e);
        }
    }

    static AtomicLong getEntityManagerCounter() {
        return entityManagerCounter;
    }
}
