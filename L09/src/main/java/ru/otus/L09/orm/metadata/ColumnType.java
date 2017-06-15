package ru.otus.L09.orm.metadata;

/**
 * Created by dzvyagin on 15.06.2017.
 */
public enum ColumnType {

    VARCHAR("VARCHAR"), BOOLEAN("BOOLEAN"), INTEGER("INTEGER"), BIGINT("BIGINT"), DECIMAL("DECIMAL"), TIMESTAMP("TIMESTAMP");

    String mysqlType;

    ColumnType(String mysqlType) {
        this.mysqlType = mysqlType;
    }

    public String getMysqlType() {
        return mysqlType;
    }
}
