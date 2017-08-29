package ru.otus.L162.messaging.messages;

import ru.otus.L162.messaging.Addressee;
import ru.otus.L162.messaging.CrudOperation;
import ru.otus.L162.model.SimpleEntity;

/**
 * Created by DocDVZ on 27.08.2017.
 */
public class DaoSocketMessage extends SocketMessage {

    private SimpleEntity requestData;
    private SimpleEntity responseData;
    private CrudOperation operation;
    private Boolean success;

    public DaoSocketMessage(Addressee from, Addressee to){
        setClassName(this.getClass().getName());
        setFrom(from);
        setTo(to);
    }

    public SimpleEntity getRequestData() {
        return requestData;
    }

    public void setRequestData(SimpleEntity requestData) {
        this.requestData = requestData;
    }

    public SimpleEntity getResponseData() {
        return responseData;
    }

    public void setResponseData(SimpleEntity responseData) {
        this.responseData = responseData;
    }

    public CrudOperation getOperation() {
        return operation;
    }

    public void setOperation(CrudOperation operation) {
        this.operation = operation;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "DaoSocketMessage{" +
                "correlationID=" + getCorrelationID() +
                ", className=" + getClassName() +
                ", from=" + getFrom() +
                ", to=" + getTo() +
                ", direction=" + getDirection() +
                ", requestData=" + requestData +
                ", responseData=" + responseData +
                ", operation=" + operation +
                ", success=" + success +
                '}';
    }
}
