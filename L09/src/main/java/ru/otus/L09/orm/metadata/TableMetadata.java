package ru.otus.L09.orm.metadata;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dzvyagin on 15.06.2017.
 */
public class TableMetadata {

    private String name;
    private Set<ColumnMetadata> columns = new HashSet<>();

    public TableMetadata(String name) {
        this.name = name;
    }

    public void addColumn(ColumnMetadata column){
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

}
