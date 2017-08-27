package ru.otus.L161.orm.metadata;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dzvyagin on 15.06.2017.
 */
public class TableMetadata {

    private Class clazz;
    private String name;
    private ColumnMetadata primaryKeyField;
    private Set<ColumnMetadata> columns = new HashSet<>();

    public TableMetadata(String name) {
        this.name = name;
    }

    public void addColumn(ColumnMetadata column) {
        columns.add(column);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ColumnMetadata> getColumns() {
        return columns;
    }

    public ColumnMetadata getPrimaryKeyField() {
        return primaryKeyField;
    }

    public void setPrimaryKeyField(ColumnMetadata primaryKeyField) {
        this.primaryKeyField = primaryKeyField;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
