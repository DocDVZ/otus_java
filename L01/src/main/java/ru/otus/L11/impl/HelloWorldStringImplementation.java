package ru.otus.L11.impl;

import ru.otus.L11.HelloWorldString;
import ru.otus.L11.IHelloWorldString;
import ru.otus.L11.StringFactory;

/**
 * Created by DocDVZ on 06.04.2017.
 */
public class HelloWorldStringImplementation implements IHelloWorldString {

    public HelloWorldString getHelloWorldString(){
        StringFactory factory = StringFactory.getInstance();
        HelloWorldString s = factory.createHelloWorldString("Hello, World!");
        return s;
    }
}
