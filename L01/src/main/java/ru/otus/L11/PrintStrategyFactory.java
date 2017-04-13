package ru.otus.L11;

import ru.otus.L11.impl.PrintStrategyImplementation;

/**
 * Created by DocDVZ on 06.04.2017.
 */
public class PrintStrategyFactory {

    private static PrintStrategyFactory instance = new PrintStrategyFactory();
    public static PrintStrategyFactory getInstance(){
        return instance;
    }


    public IPrintStrategy createIPrintStrategy(){
        IPrintStrategy printStrategy = new PrintStrategyImplementation();
        IStatusCode code = printStrategy.setupPrinting();
        if(code.getStatusCode() != 0){
            throw new RuntimeException("Failed to create ru.otus.L11.IPrintStrategy: " + code.getStatusCode());
        }
        return printStrategy;
    }
}
