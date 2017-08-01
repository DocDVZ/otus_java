package ru.otus.L15.orm;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.sf.ehcache.CacheManager;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.L15.orm.exceptions.*;
import ru.otus.L15.orm.metadata.ColumnMetadata;
import ru.otus.L15.orm.metadata.ColumnType;
import ru.otus.L15.orm.metadata.TableMetadata;

import javax.persistence.*;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
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
    private static final String DEFAULT_VARCHAR_SIZE = "(255)";
    private static final String DEFAULT_DECIMAL_SIZE = "(7,2)";
    private static final String DECIMAL_DELIMITER = ",";
    private static Map<Class<?>, TableMetadata> classesMetadata = new HashMap<>();

    private volatile boolean isInitialized = false;


    private static final Logger LOG = LoggerFactory.getLogger(OrmSessionFactory.class);

    public OrmSessionFactory(DataSource dataSource){

        this.configuration = configuration;
        try {
            this.dataSource = dataSource;
//            Class.forName(configuration.getJdbcDriver());
//            isOpen = true;
            cacheManager = CacheManager.getInstance();
//            HikariConfig hikariConfig = getHikariConfig(configuration);
//            dataSource = new HikariDataSource(hikariConfig);

            tableMetadatas = new HashSet<>();
            Reflections ref = new Reflections();

            Set<Class<?>> entityClasses = ref.getTypesAnnotatedWith(Entity.class);
            for (Class<?> clazz : entityClasses) {
                TableMetadata tableMetadata = prepareEntity(clazz);
                tableMetadatas.add(tableMetadata);
                classesMetadata.put(clazz, tableMetadata);
            }
        } catch (Exception e) {
            LOG.error("Exception occured when trying to initialize ORM provider.", e);
            throw new ORMInitializationException(e);
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
        EntityManager entityManager = new OrmSession(connection, cacheManager, classesMetadata);
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

    private TableMetadata prepareEntity(Class<?> clazz) {
        String tableName;
        if (clazz.isAnnotationPresent(Table.class)) {
            tableName = clazz.getAnnotation(Table.class).name();
        } else {
            tableName = clazz.getSimpleName();
        }
        TableMetadata tableMetadata = new TableMetadata(tableName);
        ColumnMetadata pk = null;
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Transient.class)) {
                String fieldName = defineName(field);
                ColumnType type = defineType(field);
                String size = defineSize(field, type);

                ColumnMetadata columnMetadata = new ColumnMetadata(fieldName, field.getName(), type, size);
                tableMetadata.addColumn(columnMetadata);
                if (field.isAnnotationPresent(Id.class)) {
                    if (pk != null) {
                        throw new ValidationException("Several PK found for entity " + clazz.getSimpleName());
                    } else {
                        pk = columnMetadata;
                    }
                }
            }
        }
        if (pk == null) {
            throw new ValidationException("No PK found for entity " + clazz.getSimpleName());
        }
        tableMetadata.setPrimaryKeyField(pk);
        return tableMetadata;
    }

    private String defineName(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            String name = field.getAnnotation(Column.class).name();
            if (name != null && !name.isEmpty()) {
                return name;
            } else {
                return field.getName();
            }
        } else {
            return field.getName();
        }

    }

    private String defineSize(Field field, ColumnType type) {
        if (type.equals(ColumnType.VARCHAR)) {
            if (field.isAnnotationPresent(Column.class)) {
                return "(" + field.getAnnotation(Column.class).length() + ")";
            } else {
                return DEFAULT_VARCHAR_SIZE;
            }
        } else if (type.equals(ColumnType.DECIMAL)) {
            if (field.isAnnotationPresent(Column.class)) {
                return "(" + field.getAnnotation(Column.class).precision() + DECIMAL_DELIMITER + field.getAnnotation(Column.class).scale() + ")";
            } else {
                return DEFAULT_DECIMAL_SIZE;
            }
        } else {
            return ColumnMetadata.UNDEFINED_SIZE;
        }
    }

    private ColumnType defineType(Field field) {
        Class<?> clazz = field.getType();
        if (clazz.equals(Integer.class)) {
            return ColumnType.INTEGER;
        } else if (clazz.equals(BigInteger.class)) {
            return ColumnType.BIGINT;
        } else if (clazz.equals(Long.class)){
            return ColumnType.LONG;
        } else if (clazz.equals(Boolean.class)) {
            return ColumnType.BOOLEAN;
        } else if (clazz.equals(Date.class)) {
            return ColumnType.TIMESTAMP;
        } else if (clazz.equals(Double.class) || clazz.equals(BigDecimal.class)) {
            return ColumnType.DECIMAL;
        } else if (clazz.equals(String.class)) {
            return ColumnType.VARCHAR;
        } else {
            throw new TypeNotSupportedException("Class " + clazz + " is not supported yet.");
        }
    }
}
