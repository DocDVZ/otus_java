package ru.otus.L11;

/**
 * Created by DocDVZ on 06.04.2017.
 */
public class StringFactory {

    private static StringFactory instance = new StringFactory();
    public static StringFactory getInstance(){
        return instance;
    }

    public HelloWorldString createHelloWorldString(String str){
        HelloWorldString s = new HelloWorldString();
        s.s = str;
        return s;
    }
}
