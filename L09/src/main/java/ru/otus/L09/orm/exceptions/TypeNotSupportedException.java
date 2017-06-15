package ru.otus.L09.orm.exceptions;

/**
 * Created by dzvyagin on 15.06.2017.
 */
public class TypeNotSupportedException extends RuntimeException{

    public TypeNotSupportedException(String message) {
        super(message);
    }
}
