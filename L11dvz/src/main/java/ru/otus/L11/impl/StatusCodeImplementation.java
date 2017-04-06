package ru.otus.L11.impl;

import ru.otus.L11.IStatusCode;

/**
 * Created by DocDVZ on 06.04.2017.
 */
public class StatusCodeImplementation implements IStatusCode {
    private int code;
    public StatusCodeImplementation(int code){
        this.code = code;
    }
    public int getStatusCode() {
        return code;
    }
}