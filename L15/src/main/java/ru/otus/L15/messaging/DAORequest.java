package ru.otus.L15.messaging;

import ru.otus.L15.dao.CRUDCommand;

public class DAORequest implements Request {

    private final Object payload;
    private volatile RequestResult result;
    private final CRUDCommand command;
    private final Integer id;
    private static final String CONSUMER_ID = ConsumersContext.SIMPLE_ENTITY_DAO_ID;
    private volatile boolean isReady = false;

    public DAORequest(CRUDCommand command, Object payload) {

        // TODO payload validation

        this.command = command;
        this.payload = payload;
        this.id = System.identityHashCode(this);
    }

    public Object getPayload() {
        return payload;
    }

    public RequestResult getResult() {
        if (!isReady) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void setResult(RequestResult result) {
        this.result = result;
        isReady = true;
        synchronized (this) {
            notify();
        }
    }

    @Override
    public String getConsumerID() {
        return CONSUMER_ID;
    }

    public Integer getID() {
        return id;
    }

    public CRUDCommand getCommand() {
        return command;
    }
}
