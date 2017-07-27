package ru.otus.L15.orm;


import org.reflections.Reflections;
import ru.otus.L15.orm.exceptions.TypeNotSupportedException;
import ru.otus.L15.orm.exceptions.ValidationException;
import ru.otus.L15.orm.metadata.ColumnMetadata;
import ru.otus.L15.orm.metadata.ColumnType;
import ru.otus.L15.orm.metadata.TableMetadata;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by dzvyagin on 15.06.2017.
 */
public final class OrmTool {

    private static OrmTool instance;
    private static final String DEFAULT_VARCHAR_SIZE = "(255)";
    private static final String DEFAULT_DECIMAL_SIZE = "(7,2)";
    private static final String DECIMAL_DELIMITER = ",";
    private static Map<Class<?>, TableMetadata> classesMetadata = new HashMap<>();

    private volatile boolean isInitialized = false;

    private OrmSessionFactory sessionFactory;
    private OrmConfiguration configuration;


    public OrmTool(OrmConfiguration configuration) {
        this.configuration = configuration;
    }

    public synchronized void init() {
        if (!isInitialized) {
            Set<TableMetadata> entityTables = new HashSet<>();
            Reflections ref = new Reflections();

            Set<Class<?>> entityClasses = ref.getTypesAnnotatedWith(Entity.class);
            for (Class<?> clazz : entityClasses) {
                TableMetadata tableMetadata = prepareEntity(clazz);
                entityTables.add(tableMetadata);
                classesMetadata.put(clazz, tableMetadata);
            }
            sessionFactory = new OrmSessionFactory(configuration);
            sessionFactory.setTableMetadatas(entityTables);
            sessionFactory.validateTables();
            isInitialized = true;
        }
    }

    public EntityManagerFactory getSessionFactory(){
        if (!isInitialized){
            throw new ValidationException("ORM TOOL IS NOT INITIALIZED");
        } else {
            return sessionFactory;
        }
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

    static Map<Class<?>, TableMetadata> getClassesMetadata(){
        return classesMetadata;
    }


}
