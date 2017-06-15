package ru.otus.L09.orm.metadata;

/**
 * Created by dzvyagin on 15.06.2017.
 */
public class ColumnMetadata {

    private String name;
    private ColumnType type;
    private String typeSize;

    public static final String UNDEFINED_SIZE = "";

    public ColumnMetadata(String name, ColumnType type, String typeSize) {
        this.name = name;
        this.type = type;
        this.typeSize = typeSize;
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

    public String getTypeSize() {
        return typeSize;
    }

    public void setTypeSize(String typeSize) {
        this.typeSize = typeSize;
    }
}
