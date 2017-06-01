package ru.otus.atm.exceptions;

/**
 * Created by DocDVZ on 25.05.2017.
 */
public class ThisShouldNeverHappenException extends RuntimeException{

    public ThisShouldNeverHappenException(){super();}
    public ThisShouldNeverHappenException(String message){super(message);}
}
