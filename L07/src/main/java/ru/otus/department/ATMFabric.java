package ru.otus.department;

import ru.otus.atm.impl.BiCurrencyATM;
import ru.otus.atm.impl.SimpleATM;
import ru.otus.atm.solution.MoneyContainer;
import ru.otus.atm.solution.MoneyType;
import ru.otus.atm.solution.OperationMode;

import java.util.Arrays;

/**
 * Created by DocDVZ on 25.05.2017.
 */
public class ATMFabric {

    private static ATMFabric instance;

    private ATMFabric() {
    }

    public static ATMFabric getInstance() {
        if (instance == null) {
            instance = new ATMFabric();
        }
        return instance;
    }


    public SimpleATM createDefaultSimpleATM() {
        ContainerFabric containerFabric = ContainerFabric.getInstance();

        SimpleATM atm = new SimpleATM();
        atm.addMoney(Arrays.asList(containerFabric.getContainer(MoneyType.HUNDRED),
                containerFabric.getContainer(MoneyType.FIFTY),
                containerFabric.getContainer(MoneyType.TWENTY),
                containerFabric.getContainer(MoneyType.TEN),
                containerFabric.getContainer(MoneyType.FIVE),
                containerFabric.getContainer(MoneyType.ONE)
        ));
        return atm;
    }

    public BiCurrencyATM createDefaultBiCurrencyATM() {
        BiCurrencyATM atm = new BiCurrencyATM();
        ContainerFabric containerFabric = ContainerFabric.getInstance();
        atm.setOperationMode(OperationMode.RUB);
        atm.addMoney(Arrays.asList(containerFabric.getContainer(MoneyType.HUNDRED),
                containerFabric.getContainer(MoneyType.FIFTY),
                containerFabric.getContainer(MoneyType.TWENTY),
                containerFabric.getContainer(MoneyType.TEN),
                containerFabric.getContainer(MoneyType.FIVE),
                containerFabric.getContainer(MoneyType.ONE)
        ));
        atm.setOperationMode(OperationMode.USD);
        atm.addMoney(Arrays.asList(containerFabric.getContainer(MoneyType.HUNDRED),
                containerFabric.getContainer(MoneyType.FIFTY),
                containerFabric.getContainer(MoneyType.TWENTY),
                containerFabric.getContainer(MoneyType.TEN),
                containerFabric.getContainer(MoneyType.FIVE),
                containerFabric.getContainer(MoneyType.ONE)
        ));
        return atm;
    }

}
