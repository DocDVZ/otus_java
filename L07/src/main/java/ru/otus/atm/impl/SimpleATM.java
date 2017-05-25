package ru.otus.atm.impl;

import ru.otus.atm.ATM;
import ru.otus.atm.solution.MoneyContainer;
import ru.otus.atm.exceptions.NoMoneyException;
import ru.otus.department.ATMObserver;

import javax.lang.model.util.SimpleElementVisitor6;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DocDVZ on 25.05.2017.
 */
public class SimpleATM implements ATM {

    private List<MoneyContainer> containers;
    private MoneyContainer firstContainer;
    private List<ATMObserver> observers = new ArrayList<>();

    /**
     * replaces money containers with new ones
     *
     * @param containers
     */
    public void addMoney(List<MoneyContainer> containers) {
        containers.sort((p, k) -> p.getMoneyType().compareTo(k.getMoneyType()));
        this.containers = containers;
        this.firstContainer = containers.size() == 0 ? null : containers.get(0);
        MoneyContainer prev = null;
        for (MoneyContainer container : containers) {
            if (prev != null) {
                prev.setNextContainer(container);
            }
            prev = container;
        }
    }


    public Integer getMoney(Integer amount) {
        try {
            firstContainer.withdrawMoney(amount);
            return amount;
        } catch (NoMoneyException e) {
            containers.forEach(p -> p.rollbackState());
            observers.forEach(p -> p.onEvent(this));
            System.out.println("Not enough money in ATM");
            return 0;
        }
    }

    public Integer getBalance() {
        Integer balance = 0;
        if (containers!=null) {
            for (MoneyContainer container : containers) {
                balance += container.getBalance();
            }
        }
        return balance;
    }

    @Override
    public void registerObserver(ATMObserver observer) {
        this.observers.add(observer);
    }

}
