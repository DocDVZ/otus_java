package ru.otus.atm.exceptions;

/**
 * Created by DocDVZ on 25.05.2017.
 */
public class NoMoneyException extends RuntimeException {
    public NoMoneyException(String message) {
        super(message);
    }

    public NoMoneyException(){super();}
}
