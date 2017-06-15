package ru.otus.L09.orm;

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

/**
 * Created by dzvyagin on 15.06.2017.
 */
public class OrmSessionFactory implements EntityManagerFactory {

    private OrmConfiguration configuration;
    private Connection connection;
    private volatile boolean isOpen = false;
    private Set<TableMetadata> tableMetadatas;

    OrmSessionFactory(OrmConfiguration configuration){
        this.configuration = configuration;
        try {
            Class.forName(configuration.getJdbcDriver());
            connection = DriverManager.getConnection(configuration.getDbUrl(), configuration.getUser(), configuration.getPassword());
            isOpen = true;
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            throw new ConnectionException(e);
        }
    }

    @Override
    public EntityManager createEntityManager() {
        Statement statement;
        try {
            statement = connection.createStatement();
        } catch (SQLException e){
            e.printStackTrace();
            throw new ConnectionException(e);
        }
        EntityManager entityManager = new OrmSession(statement);
        return entityManager;
    }

    @Override
    public EntityManager createEntityManager(Map map) {
        throw new NotImplementedException();
    }

    @Override
    public void close() {
        try {
            connection.close();
            isOpen = false;
        } catch (SQLException e){
            e.printStackTrace();
            throw new ConnectionException(e);
        }
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
        try( Statement statement = connection.createStatement()) {
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

}
