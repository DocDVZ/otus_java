package ru.otus.L162.messaging.messages;

import ru.otus.L162.messaging.Addressee;

/**
 * Created by DocDVZ on 27.08.2017.
 */
public abstract class SocketMessage {

    public static final String CLASS_NAME_VARIABLE = "className";

    private String correlationID;
    private String className = this.getClass().getName();
    private Addressee from;
    private Addressee to;
    private MessageDirection direction;

    public String getCorrelationID() {
        return correlationID;
    }

    public void setCorrelationID(String correlationID) {
        this.correlationID = correlationID;
    }

    public MessageDirection getDirection() {
        return direction;
    }

    public void setDirection(MessageDirection direction) {
        this.direction = direction;
    }

    protected void setClassName(String className) {
        this.className = className;
    }

    protected String getClassName() {
        return className;
    }

    public Addressee getFrom() {
        return from;
    }

    public void setFrom(Addressee from) {
        this.from = from;
    }

    public Addressee getTo() {
        return to;
    }

    public void setTo(Addressee to) {
        this.to = to;
    }


}
