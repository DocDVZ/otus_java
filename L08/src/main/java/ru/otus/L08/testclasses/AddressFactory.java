package ru.otus.L08.testclasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by dzvyagin on 01.06.2017.
 */
public class AddressFactory {

    private static AddressFactory instance;

    private List<String> countries;
    private List<String> cities;
    private List<String> streets;


    private AddressFactory(){
        countries = Arrays.asList("USA", "GER", "BEL", "RUS", "UKR", "SPN", "MEX", "FRA");
        cities = Arrays.asList("Moscow", "Berlin", "Paris", "New-York", "Los-Angeles", "Volgograd", "Kiev", "Minsk");
        streets = Arrays.asList("Lenin st", "Rousvelt st", "Mira st", "Star blvrd", "Trump st", "Washington st", "Main st");
    }


    public static AddressFactory getInstance(){
        if (instance == null){
            instance = new AddressFactory();
        }
        return  instance;
    }

    public Address generateAddress(){
        Address address = new Address();
        Random random = new Random();
        address.setCountryISOCode(EntityUtils.getRandomElement(countries));
        address.setCity(EntityUtils.getRandomElement(cities));
        address.setHouse(String.valueOf(random.nextInt(200)));
        address.setZipCode(String.valueOf(99 + random.nextInt(900)) + String.valueOf(99 + random.nextInt(900)));
        address.setStreet(EntityUtils.getRandomElement(streets));
        return address;
    }



}
