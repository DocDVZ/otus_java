package ru.otus.L15.messaging;

import ru.otus.L15.dao.CRUDCommand;

public class DAORequest implements Request {

    private final Object payload;
    private volatile RequestResult result;
    private final CRUDCommand command;
    private final Integer id;
    private static final String CONSUMER_ID = ConsumersContext.SIMPLE_ENTITY_DAO_ID;
    private volatile boolean isReady = false;
    private Object lock = new Object();

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
        synchronized (lock) {
            if (!isReady) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public void setResult(RequestResult result) {
        this.result = result;
        synchronized (lock) {
            isReady = true;
            lock.notify();
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
