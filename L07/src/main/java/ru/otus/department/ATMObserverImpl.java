package ru.otus.department;

import ru.otus.atm.ATM;

/**
 * Created by DocDVZ on 26.05.2017.
 */
public class ATMObserverImpl implements ATMObserver{

    @Override
    public void onEvent(ATM atm) {
        System.out.println("OBSERVER WARNING: ATM got a NoMoneyException, money left: " + atm.getBalance());
    }
}
