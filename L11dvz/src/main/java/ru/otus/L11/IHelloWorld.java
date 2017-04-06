package ru.otus.L11;

/**
 * Created by DocDVZ on 06.04.2017.
 */
public interface IHelloWorld {
    public IHelloWorldString getHelloWorld();
    public IPrintStrategy getPrintStrategy();
    public IStatusCode print(IPrintStrategy strategy, IHelloWorldString toPrint);
}
