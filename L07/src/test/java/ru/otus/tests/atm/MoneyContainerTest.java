package ru.otus.tests.atm;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.otus.atm.exceptions.NoMoneyException;
import ru.otus.atm.solution.MoneyContainer;
import ru.otus.atm.solution.MoneyType;

/**
 * Created by DocDVZ on 25.05.2017.
 */

public class MoneyContainerTest extends Assert {

    private MoneyContainer current;
    private MoneyContainer next;

    @Before
    public void prepareTest(){
        current = new MoneyContainer(MoneyType.FIVE, 10);
        next = new MoneyContainer(MoneyType.ONE, 10);
        current.setNextContainer(next);
    }

    @Test
    public void getMoneyTest(){
        assertEquals(current.withdrawMoney(14), Integer.valueOf(14));
    }


    @Test(expected = NoMoneyException.class)
    public void notEnoughMoneyTest(){
        current.withdrawMoney(Integer.MAX_VALUE);
    }

    @Test
    public void getBalanceTest(){
        assertEquals(current.getBalance(), Integer.valueOf(50));
    }

}
