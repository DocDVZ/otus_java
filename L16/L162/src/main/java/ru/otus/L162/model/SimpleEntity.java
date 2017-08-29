package ru.otus.L162.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by DocDVZ on 15.06.2017.
 */
@Entity
@Table(name = "SIMPLE_ENTITY")
public class SimpleEntity {

    @Id
    @Column(name = "INT_FIELD")
    private Integer intField;

    @Column(name = "STR_FIELD", length = 40)
    private String strField;
    private Boolean boolField;
    @Transient
    private Long longField;
    @Column(name = "BIG_DECIMAL_FIELD", precision = 7, scale = 2)
    private BigDecimal bigDecimalField;
    private BigInteger bigIntegerField;

    public Integer getIntField() {
        return intField;
    }

    public void setIntField(Integer intField) {
        this.intField = intField;
    }

    public String getStrField() {
        return strField;
    }

    public void setStrField(String strField) {
        this.strField = strField;
    }

    public Boolean getBoolField() {
        return boolField;
    }

    public void setBoolField(Boolean boolField) {
        this.boolField = boolField;
    }

    public Long getLongField() {
        return longField;
    }

    public void setLongField(Long longField) {
        this.longField = longField;
    }

    public BigDecimal getBigDecimalField() {
        return bigDecimalField;
    }

    public void setBigDecimalField(BigDecimal bigDecimalField) {
        this.bigDecimalField = bigDecimalField;
    }

    public BigInteger getBigIntegerField() {
        return bigIntegerField;
    }

    public void setBigIntegerField(BigInteger bigIntegerField) {
        this.bigIntegerField = bigIntegerField;
    }



    @Override
    public String toString() {
        return "SimpleEntity{" +
                "intField=" + intField +
                ", strField='" + strField + '\'' +
                ", boolField=" + boolField +
                ", longField=" + longField +
                ", bigDecimalField=" + bigDecimalField +
                ", bigIntegerField=" + bigIntegerField +
                '}';
    }
}
