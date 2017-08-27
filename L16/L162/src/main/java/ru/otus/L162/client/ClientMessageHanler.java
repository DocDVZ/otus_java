package ru.otus.L162.client;

import ru.otus.L162.messaging.messages.SocketMessage;

import java.util.concurrent.TimeoutException;

/**
 * Created by DocDVZ on 27.08.2017.
 */
public interface ClientMessageHanler<T extends SocketMessage> {

    String send(T message);

    T getResponse(String id) throws TimeoutException;


}
