package ru.otus.department;

import ru.otus.atm.solution.MoneyContainer;
import ru.otus.atm.solution.MoneyType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DocDVZ on 26.05.2017.
 */
public class ContainerFabric {

    private static ContainerFabric instance;
    private Map<MoneyType, MoneyContainer> cache;

    private ContainerFabric(){}

    private ContainerFabric( Map<MoneyType, MoneyContainer> cache){
        this.cache = cache;
    }

    public static ContainerFabric getInstance(){
        if (instance==null){
            Map<MoneyType, MoneyContainer> containerMap = new HashMap<>();
            containerMap.put(MoneyType.ONE, new MoneyContainer(MoneyType.ONE, 100));
            containerMap.put(MoneyType.FIVE, new MoneyContainer(MoneyType.FIVE, 100));
            containerMap.put(MoneyType.TEN, new MoneyContainer(MoneyType.TEN, 100));
            containerMap.put(MoneyType.TWENTY, new MoneyContainer(MoneyType.TWENTY, 100));
            containerMap.put(MoneyType.FIFTY, new MoneyContainer(MoneyType.FIFTY, 100));
            containerMap.put(MoneyType.HUNDRED, new MoneyContainer(MoneyType.HUNDRED, 100));
            instance = new ContainerFabric(containerMap);
        }
        return instance;
    }

    public MoneyContainer getContainer(MoneyType type){
        return cache.get(type).getClone();
    }
}
