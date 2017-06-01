package ru.otus.L08.testclasses;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by dzvyagin on 01.06.2017.
 */
public class ClientFactory {

    private static ClientFactory instance;

    private AddressFactory addressFactory = AddressFactory.getInstance();
    private List<String> firstNames;
    private List<String> lastNames;

    private ClientFactory(){
        firstNames = Arrays.asList("John", "Ivan", "Joseph", "Alexander", "Alex", "Arnold", "Jean", "Viktor");
        lastNames = Arrays.asList("Johnson", "Richardson", "Ivanov", "Petrov", "Sidorov", "Wilson", "Lucky");
    }


    public static ClientFactory getInstance(){
        if (instance == null){
            instance = new ClientFactory();
        }
        return  instance;
    }

    public Client getClient(){
        Client client = new Client();
        client.setHomeAddress(addressFactory.generateAddress());
        client.setRegisterAddress(addressFactory.generateAddress());
        client.setFirstName(EntityUtils.getRandomElement(firstNames));
        client.setSecondName(EntityUtils.getRandomElement(lastNames));
        client.setBithDate(new Date());
        return client;
    }

}
