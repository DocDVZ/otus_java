package ru.otus.L09.orm;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dzvyagin on 15.06.2017.
 */
public class TableData {

    private String name;
    private Set<ColumnData> columns = new HashSet<>();

    public TableData(String name) {
        this.name = name;
    }

    public void addColumn(ColumnData column){
        columns.add(column);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
