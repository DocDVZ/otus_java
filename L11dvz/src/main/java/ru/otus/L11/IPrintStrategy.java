package ru.otus.L11;

import ru.otus.L11.IHelloWorldString;
import ru.otus.L11.IStatusCode;

/**
 * Created by DocDVZ on 06.04.2017.
 */
public interface IPrintStrategy {
    public IStatusCode setupPrinting();
    public IStatusCode print(IHelloWorldString string);
}
