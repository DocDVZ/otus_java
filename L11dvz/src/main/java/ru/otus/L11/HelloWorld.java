package ru.otus.L11;

/**
 * Created by DocDVZ on 06.04.2017.
 */
public class HelloWorld {
    private static HelloWorld instance;

    public static void main(String[] args){
        instantiateHelloWorldMainClassAndRun();
    }

    public static void instantiateHelloWorldMainClassAndRun(){
        instance = new HelloWorld();
    }

    public HelloWorld(){
        HelloWorldFactory factory = HelloWorldFactory.getInstance();
        IHelloWorld helloWorld = factory.createHelloWorld();
        IHelloWorldString helloWorldString = helloWorld.getHelloWorld();
        IPrintStrategy printStrategy = helloWorld.getPrintStrategy();
        IStatusCode code = helloWorld.print(printStrategy, helloWorldString);
        if(code.getStatusCode() != 0){
            throw new RuntimeException("Failed to print: " + code.getStatusCode());
        }
    }
}
