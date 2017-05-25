package ru.otus.atm;

import ru.otus.atm.solution.MoneyContainer;
import ru.otus.department.ATMObserver;

import java.util.List;

/**
 * Created by DocDVZ on 25.05.2017.
 */
public interface ATM {

    void addMoney(List<MoneyContainer> containers);
    Integer getMoney(Integer amount);
    Integer getBalance();
    void registerObserver(ATMObserver observer);

}
