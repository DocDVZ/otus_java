package ru.otus.L08.testclasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by DocDVZ on 01.06.2017.
 */
public class AddressCollectionContainerFactory {

    private static AddressCollectionContainerFactory instance;
    private AddressFactory addressFactory = AddressFactory.getInstance();
    private Random random = new Random();

    private AddressCollectionContainerFactory(){}

    public static AddressCollectionContainerFactory getInstance(){
        if (instance == null){
            instance = new AddressCollectionContainerFactory();
        }
        return  instance;
    }

    public AddressCollectionContainer getAddressCollectionContainer(){
        AddressCollectionContainer container = new AddressCollectionContainer();
        Integer addrAmount = random.nextInt(10) + 1;
        List<Address> addresses = new ArrayList<>();
        for (int i=0; i<addrAmount; i++){
            addresses.add(addressFactory.generateAddress());
        }
        container.setName("Container of " + addrAmount + " addresses");
        container.setAddresses(addresses);
        Address[] addressesArr = {addressFactory.generateAddress(), addressFactory.generateAddress()};
        container.setAddressesArr(addressesArr);
        return container;
    }

}
