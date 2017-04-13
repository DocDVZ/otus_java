package ru.otus.L11;

import ru.otus.L11.impl.HelloWorldImplementation;

/**
 * Created by DocDVZ on 06.04.2017.
 */
public class HelloWorldFactory {

    private static HelloWorldFactory instance = new HelloWorldFactory();

    public static HelloWorldFactory getInstance(){
        return instance;
    }

    public IHelloWorld createHelloWorld(){
        IHelloWorld helloWorld = new HelloWorldImplementation();
        return helloWorld;
    }
}
