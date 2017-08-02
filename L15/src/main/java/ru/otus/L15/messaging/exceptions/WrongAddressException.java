package ru.otus.L15.messaging.exceptions;

public class WrongAddressException extends RuntimeException {

    public WrongAddressException() {
    }

    public WrongAddressException(String message) {
        super(message);
    }

    public WrongAddressException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongAddressException(Throwable cause) {
        super(cause);
    }

    public WrongAddressException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
