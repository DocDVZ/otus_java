package ru.otus.department;

import ru.otus.atm.ATM;

/**
 * Created by DocDVZ on 26.05.2017.
 */
public interface ATMObserver {

    void onEvent(ATM atm);
}
