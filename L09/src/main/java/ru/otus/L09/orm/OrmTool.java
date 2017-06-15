package ru.otus.L09.orm;


import org.reflections.Reflections;
import ru.otus.L09.orm.exceptions.TypeNotSupportedException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

/**
 * Created by dzvyagin on 15.06.2017.
 */
public final class OrmTool {

    private static OrmTool intstance;
    private static final String DEFAULT_VARCHAR_SIZE = "255";
    private static final String DEFAULT_DECIMAL_SIZE = "7;2";
    private static final String DECIMAL_DELIMITER = "0;0";

    private volatile boolean isInitialized = false;
    private Set<TableData> entityTables;


    private OrmTool() {
    }

    public static OrmTool getInstance() {
        if (intstance == null) {
            intstance = new OrmTool();
        }
        return intstance;
    }

    public synchronized void init() {
        if (!isInitialized) {
            Reflections ref = new Reflections();
            Set<Class<?>> entityClasses = ref.getTypesAnnotatedWith(Entity.class);
            for (Class<?> clazz : entityClasses) {
                entityTables.add(prepareEntity(clazz));
            }

            isInitialized = true;
        }
    }


    private TableData prepareEntity(Class<?> clazz) {
        String tableName;
        if (clazz.isAnnotationPresent(Table.class)) {
            tableName = clazz.getAnnotation(Table.class).name();
        } else {
            tableName = clazz.getSimpleName();
        }
        TableData tableData = new TableData(tableName);
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Transient.class)) {
                String fieldName = field.getName();
                ColumnType type = defineType(field);
                String size = defineSize(field, type);
                ColumnData columnData = new ColumnData(fieldName, type, size);
                tableData.addColumn(columnData);
            }
        }
        return tableData;
    }

    private String defineSize(Field field, ColumnType type) {
        if (type.equals(ColumnType.VARCHAR)) {
            if (field.isAnnotationPresent(Column.class)) {
                return String.valueOf(field.getAnnotation(Column.class).length());
            } else {
                return DEFAULT_VARCHAR_SIZE;
            }
        } else if (type.equals(ColumnType.DECIMAL)) {
            if (field.isAnnotationPresent(Column.class)) {
                return field.getAnnotation(Column.class).precision() + DECIMAL_DELIMITER + field.getAnnotation(Column.class).scale();
            } else {
                return DEFAULT_DECIMAL_SIZE;
            }
        } else {
            return ColumnData.UNDEFINED_SIZE;
        }
    }

    private ColumnType defineType(Field field) {
        Class<?> clazz = field.getType();
        if (clazz.equals(Integer.class)) {
            return ColumnType.INTEGER;
        } else if (clazz.equals(Long.class) || clazz.equals(BigInteger.class)) {
            return ColumnType.BIGINT;
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
