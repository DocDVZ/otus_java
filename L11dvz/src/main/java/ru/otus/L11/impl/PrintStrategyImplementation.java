package ru.otus.L11.impl;

import ru.otus.L11.IHelloWorldString;
import ru.otus.L11.IPrintStrategy;
import ru.otus.L11.IStatusCode;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by DocDVZ on 06.04.2017.
 */
public class PrintStrategyImplementation implements IPrintStrategy {
    private OutputStream print;
    public IStatusCode setupPrinting() {
        try{
            FileDescriptor descriptor = FileDescriptor.out;
            print = new FileOutputStream(descriptor);
            return new StatusCodeImplementation(0);
        }
        catch(Exception e){
            return new StatusCodeImplementation(-1);
        }
    }
    public IStatusCode print(IHelloWorldString string) {
        try{
            print.write(string.getHelloWorldString().getHelloWorldString().concat("\n").getBytes("UTF-8"));
            return new StatusCodeImplementation(0);
        }
        catch(Exception e){
            return new StatusCodeImplementation(-1);
        }
    }
}
