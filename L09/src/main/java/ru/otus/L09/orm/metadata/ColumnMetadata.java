package ru.otus.L09.orm.metadata;

/**
 * Created by dzvyagin on 15.06.2017.
 */
public class ColumnMetadata {

    private String name;
    private ColumnType type;
    private String size;

    public static final String UNDEFINED_SIZE = "no size";

    public ColumnMetadata(String name, ColumnType type, String size) {
        this.name = name;
        this.type = type;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ColumnType getType() {
        return type;
    }

    public void setType(ColumnType type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
