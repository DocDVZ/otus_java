package ru.otus.L09.orm;

import ru.otus.L09.orm.exceptions.ConnectionException;
import ru.otus.L09.orm.exceptions.NotImplementedException;
import ru.otus.L09.orm.exceptions.ValidationException;
import ru.otus.L09.orm.metadata.ColumnMetadata;
import ru.otus.L09.orm.metadata.TableMetadata;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by dzvyagin on 15.06.2017.
 */
public class OrmSession implements EntityManager {

    private Statement statement;
    private boolean isOpen = true;
    private Map<Class<?>, TableMetadata> classesMetadata;

    public OrmSession(Statement statement) {
        //TODO fix bad relationship
        this.classesMetadata = OrmTool.getInstance().classesMetadata;
        this.statement = statement;
    }

    private boolean isAcceptable(Object o) {
        if (!classesMetadata.containsKey(o.getClass())) {
            throw new ValidationException("Object is not an entity! Class: " + o.getClass());
        }
        if (!isOpen) {
            throw new ConnectionException("EntityManager is closed!");
        }
        return true;
    }

    private boolean isAcceptable(Class<?> clazz) {
        if (!classesMetadata.containsKey(clazz)) {
            throw new ValidationException("Object is not an entity! Class: " + clazz);
        }
        if (!isOpen) {
            throw new ConnectionException("EntityManager is closed!");
        }
        return true;
    }


    @Override
    public void persist(Object o) {
        if (isAcceptable(o)) {
            TableMetadata tableMetadata = classesMetadata.get(o.getClass());
            StringBuilder sql = new StringBuilder("INSERT INTO `" + tableMetadata.getName() + "` (");
            String prefix = "";
            for (ColumnMetadata columnMetadata : tableMetadata.getColumns()) {
                sql.append(prefix);
                sql.append("`" + columnMetadata.getName() + "`");
                prefix = ",";
            }
            sql.append(") VALUES (");
            prefix = "";
            for (ColumnMetadata columnMetadata : tableMetadata.getColumns()) {
                sql.append(prefix);
                sql.append(getMysqlValue(o, columnMetadata));
                prefix = ",";
            }
            sql.append(");");
            executeSql(sql.toString());
        }
    }


    @Override
    public <T> T merge(T t) {
        if (isAcceptable(t)) {
            TableMetadata tableMetadata = classesMetadata.get(t.getClass());
            StringBuilder sql = new StringBuilder("UPDATE `" + tableMetadata.getName() + "` SET ");
            String prefix = "";
            for (ColumnMetadata columnMetadata : tableMetadata.getColumns()) {
                sql.append(prefix);
                sql.append("`" + columnMetadata.getName() + "`=");
                sql.append(getMysqlValue(t, columnMetadata));
                prefix = ",";
            }
            ColumnMetadata pk = tableMetadata.getPrimaryKeyField();
            sql.append(" WHERE `" + pk.getName() + "`=" + getMysqlValue(t, pk));
            sql.append(";");
            executeSql(sql.toString());
        }
        return t;
    }


    @Override
    public void remove(Object o) {
        if (isAcceptable(o)) {
            TableMetadata tableMetadata = classesMetadata.get(o.getClass());
            StringBuilder sql = new StringBuilder("DELETE FROM `" + tableMetadata.getName() + "`");
            ColumnMetadata pk = tableMetadata.getPrimaryKeyField();
            sql.append(" WHERE `" + pk.getName() + "`=" + getMysqlValue(o, pk));
            sql.append(";");
            executeSql(sql.toString());
        }
    }

    @Override
    public <T> T find(Class<T> aClass, Object o) {
        T result = null;
        if (isAcceptable(aClass)) {
            TableMetadata tableMetadata = classesMetadata.get(aClass);
            ColumnMetadata pk = tableMetadata.getPrimaryKeyField();
            try {
                result = aClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ValidationException(e);
            }
            // TODO change * to on fieldnames
            StringBuilder sql = new StringBuilder("SELECT * from `" + tableMetadata.getName() + "`");
            ResultSet resultSet = executeSqlWithResultSet(sql.toString());

            try {
                boolean isEmpty = true;
                while (resultSet.next()) {
                    isEmpty = false;
                    for (ColumnMetadata columnMetadata : tableMetadata.getColumns()) {
                        Field field = null;
                        boolean isAccessible = false;
                        try {
                            field = aClass.getDeclaredField(columnMetadata.getFieldName());
                            isAccessible = field.isAccessible();
                            field.setAccessible(true);
                            switch (columnMetadata.getType()) {
                                case LONG:
                                    field.set(result, resultSet.getLong(columnMetadata.getName()));
                                    break;
                                case BIGINT:
                                    field.set(result, BigInteger.valueOf(resultSet.getLong(columnMetadata.getName())));
                                    break;
                                case DECIMAL:
                                    field.set(result, resultSet.getBigDecimal(columnMetadata.getName()));
                                    break;
                                case BOOLEAN:
                                    field.set(result, resultSet.getBoolean(columnMetadata.getName()));
                                    break;
                                case INTEGER:
                                    field.set(result, resultSet.getInt(columnMetadata.getName()));
                                    break;
                                case VARCHAR:
                                    field.set(result, resultSet.getString(columnMetadata.getName()));
                                    break;
                                case TIMESTAMP:
                                    field.set(result, resultSet.getDate(columnMetadata.getName()));
                                    break;
                                default:
                                    throw new ValidationException("UNKNOWN FIELD TYPE");
                            }
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            throw new ValidationException(e);
                        } finally {
                            if (field != null) {
                                field.setAccessible(isAccessible);
                            }
                        }

                    }
                }
                if (isEmpty){
                    return null;
                }
            } catch (SQLException e) {
                throw new ConnectionException(e);
            }


        }

        return result;
    }

    @Override
    public <T> T getReference(Class<T> aClass, Object o) {
        throw new NotImplementedException();
    }

    @Override
    public void flush() {
        throw new NotImplementedException();
    }

    @Override
    public void setFlushMode(FlushModeType flushModeType) {
        throw new NotImplementedException();
    }

    @Override
    public FlushModeType getFlushMode() {
        throw new NotImplementedException();
    }

    @Override
    public void lock(Object o, LockModeType lockModeType) {
        throw new NotImplementedException();
    }

    @Override
    public void refresh(Object o) {
        throw new NotImplementedException();
    }

    @Override
    public void clear() {
        throw new NotImplementedException();
    }

    @Override
    public boolean contains(Object o) {
        throw new NotImplementedException();
    }

    @Override
    public Query createQuery(String s) {
        throw new NotImplementedException();
    }

    @Override
    public Query createNamedQuery(String s) {
        throw new NotImplementedException();
    }

    @Override
    public Query createNativeQuery(String s) {
        throw new NotImplementedException();
    }

    @Override
    public Query createNativeQuery(String s, Class aClass) {
        throw new NotImplementedException();
    }

    @Override
    public Query createNativeQuery(String s, String s1) {
        throw new NotImplementedException();
    }

    @Override
    public void joinTransaction() {
        throw new NotImplementedException();
    }

    @Override
    public Object getDelegate() {
        throw new NotImplementedException();
    }

    @Override
    public void close() {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ConnectionException(e);
        }
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public EntityTransaction getTransaction() {
        throw new NotImplementedException();
    }

    private String getMysqlValue(Object o, ColumnMetadata columnMetadata) {
        Field field = null;
        boolean comma = false;
        String result = "";
        boolean isAccessible = false;
        try {
            field = o.getClass().getDeclaredField(columnMetadata.getFieldName());
            isAccessible = field.isAccessible();
            if (field.getType().isAssignableFrom(Date.class) || field.getType().isAssignableFrom(String.class)) {
                comma = true;
            }
            if (comma) {
                result += "'";
            }
            field.setAccessible(true);
            Object value = field.get(o);
            String s = value.toString();
            if (value.getClass().isAssignableFrom(Date.class)) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                s = df.format((Date) value);
            }
            result += s;
            if (comma) {
                result += "'";
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ValidationException(e);
        } finally {
            if (field != null) {
                field.setAccessible(isAccessible);
            }
        }
        return result;
    }

    private void executeSql(String sql) {
        try {
            System.out.println("Executing sql: " + sql);
            statement.execute(sql.toString());
        } catch (SQLException e) {
            throw new ConnectionException(e);
        }
    }

    private ResultSet executeSqlWithResultSet(String sql) {
        try {
            System.out.println("Executing sql: " + sql);
            statement.execute(sql.toString());
            return statement.getResultSet();
        } catch (SQLException e) {
            throw new ConnectionException(e);
        }
    }

}
