package ru.otus.atm.solution;

/**
 * Created by DocDVZ on 25.05.2017.
 */
public enum MoneyType implements Comparable<MoneyType> {
    HUNDRED(100), FIFTY(50), TWENTY(20), TEN(10), FIVE(5), ONE(1);

    private Integer nominal;

    MoneyType(Integer nominal) {
        this.nominal = nominal;
    }

    public Integer getNominal(){
        return new Integer(nominal);
    }

}
