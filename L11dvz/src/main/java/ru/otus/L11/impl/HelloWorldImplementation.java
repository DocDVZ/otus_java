package ru.otus.L11.impl;

import ru.otus.L11.*;

/**
 * Created by DocDVZ on 06.04.2017.
 */
public class HelloWorldImplementation implements IHelloWorld {

    public IHelloWorldString getHelloWorld() {
        IHelloWorldString string = new HelloWorldStringImplementation();
        return string;
    }
    public IPrintStrategy getPrintStrategy() {
        PrintStrategyFactory factory = PrintStrategyFactory.getInstance();
        return factory.createIPrintStrategy();
    }
    public IStatusCode print(IPrintStrategy strategy, IHelloWorldString toPrint) {
        IStatusCode code = strategy.print(toPrint);
        return code;
    }
}