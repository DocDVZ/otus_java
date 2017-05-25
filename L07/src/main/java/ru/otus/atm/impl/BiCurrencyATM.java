package ru.otus.atm.impl;


import ru.otus.atm.ATM;
import ru.otus.atm.exceptions.ThisShouldNeverHappenException;
import ru.otus.atm.solution.MoneyContainer;
import ru.otus.atm.solution.OperationMode;
import ru.otus.department.ATMObserver;

import java.util.List;

/**
 * Created by DocDVZ on 25.05.2017.
 */
public class BiCurrencyATM implements ATM{

    private OperationMode operationMode = OperationMode.RUB;
    private ATM rubATM = new SimpleATM();
    private ATM usdATM = new SimpleATM();

    public void setOperationMode(OperationMode mode){
        this.operationMode = mode;
    }

    public OperationMode getOperationMode(){
        return this.operationMode;
    }


    @Override
    public void addMoney(List<MoneyContainer> containers) {
        switch (operationMode){
            case RUB: rubATM.addMoney(containers);break;
            case USD: usdATM.addMoney(containers);break;
            default:
                throw new ThisShouldNeverHappenException("ATM does not support this currency");
        }
    }

    @Override
    public Integer getMoney(Integer amount) {
        switch (operationMode){
            case RUB: return rubATM.getMoney(amount);
            case USD: return usdATM.getMoney(amount);
            default:
                throw new ThisShouldNeverHappenException("ATM does not support this currency");
        }
    }

    @Override
    public Integer getBalance() {
        switch (operationMode){
            case RUB: return rubATM.getBalance();
            case USD: return usdATM.getBalance();
            default:
                throw new ThisShouldNeverHappenException("ATM does not support this currency");
        }
    }

    @Override
    public void registerObserver(ATMObserver observer) {
        rubATM.registerObserver(observer);
        usdATM.registerObserver(observer);
    }


}
