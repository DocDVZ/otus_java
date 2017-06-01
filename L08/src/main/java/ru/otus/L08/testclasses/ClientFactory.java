package ru.otus.L08.testclasses;

import java.util.*;

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
        client.setBirthDate(new Date());
        client.setPhones(generatePhones());
        return client;
    }

    private Set<String> generatePhones(){
        Random random = new Random();
        Set<String> result = new HashSet<>();
        Integer num = random.nextInt(5);
        for (int i = 0; i<num; i++){
            String phone = "";
            for (int k = 0; k<10; k++){
                phone+=random.nextInt(9);
            }
            result.add(phone);
        }
        return result;
    }

}
