package ru.otus.L15.messaging;

public interface Request {

    Integer getID();
    Object getPayload();
    RequestResult getResult();
    void setResult(RequestResult result);
    String getConsumerID();

}
