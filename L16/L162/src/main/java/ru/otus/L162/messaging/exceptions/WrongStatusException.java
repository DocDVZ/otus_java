package ru.otus.L162.messaging.exceptions;

/**
 * Created by DocDVZ on 17.08.2017.
 */
public class WrongStatusException extends RuntimeException {

    public WrongStatusException() {
    }

    public WrongStatusException(String message) {
        super(message);
    }

    public WrongStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongStatusException(Throwable cause) {
        super(cause);
    }

    public WrongStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}