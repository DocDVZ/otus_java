package ru.otus;

import ru.otus.atm.ATM;
import ru.otus.atm.impl.SimpleATM;
import ru.otus.atm.solution.MoneyContainer;
import ru.otus.atm.solution.MoneyType;
import ru.otus.department.ATMFabric;
import ru.otus.department.ATMObserver;
import ru.otus.department.ATMObserverImpl;

import java.util.Arrays;

/**
 * Created by DocDVZ on 25.05.2017.
 */
public class Main {


    public static void main(String[] args) {

        ATMFabric atmFabric = ATMFabric.getInstance();
        ATMObserver observer = new ATMObserverImpl();

        ATM atm = atmFabric.createDefaultSimpleATM();
        atm.registerObserver(observer);

        System.out.println("Current atm balance is " + atm.getBalance());
        System.out.println("Getting 4332 money " + atm.getMoney(4332));
        System.out.println("Current atm balance is " + atm.getBalance());
        System.out.println("Getting too much money " + atm.getMoney(999999));


    }
}
