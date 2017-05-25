package ru.otus.tests.atm;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.otus.atm.ATM;
import ru.otus.atm.impl.SimpleATM;
import ru.otus.atm.solution.MoneyContainer;
import ru.otus.atm.solution.MoneyType;

import java.util.Arrays;

/**
 * Created by DocDVZ on 25.05.2017.
 */
public class SimpleATMTest extends Assert {

    private ATM atm;

    @Before
    public void prepareATM(){
        atm = new SimpleATM();
        atm.addMoney(Arrays.asList(new MoneyContainer(MoneyType.HUNDRED, 100),
                new MoneyContainer(MoneyType.FIFTY, 100),
                new MoneyContainer(MoneyType.TWENTY, 100),
                new MoneyContainer(MoneyType.TEN, 100),
                new MoneyContainer(MoneyType.FIVE, 100),
                new MoneyContainer(MoneyType.ONE, 100)
        ));
    }

    @Test
    public void addMoney() throws Exception {
        atm.addMoney(Arrays.asList(new MoneyContainer(MoneyType.TWENTY, 100)));
        assertEquals(atm.getBalance(), Integer.valueOf(2000));
    }

    @Test
    public void getMoney(){
        assertEquals(atm.getMoney(100), Integer.valueOf(100));
    }

    @Test
    public void getTooMuchMoney(){
        assertEquals(atm.getMoney(Integer.MAX_VALUE), Integer.valueOf(0));
    }

    @Test
    public void getBalance() throws Exception {
        Integer initialBalance = atm.getBalance();
        atm.getMoney(1234);
        assertEquals(Integer.valueOf(initialBalance - 1234), atm.getBalance());
    }


}
