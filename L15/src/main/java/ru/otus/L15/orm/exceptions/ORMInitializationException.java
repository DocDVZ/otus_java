package ru.otus.L15.orm.exceptions;

/**
 * Created by DocDVZ on 01.08.2017.
 */
public class ORMInitializationException extends RuntimeException {

    public ORMInitializationException() {
    }

    public ORMInitializationException(String message) {
        super(message);
    }

    public ORMInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ORMInitializationException(Throwable cause) {
        super(cause);
    }

    public ORMInitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}