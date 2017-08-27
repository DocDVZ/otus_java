package ru.otus.L162.channel;

import ru.otus.L162.messaging.messages.SocketMessage;

/**
 * Created by DocDVZ on 16.08.2017.
 */
public interface MessageChannel<T extends SocketMessage> {

    void send(T message);

    T take() throws InterruptedException;

    T poll();

    void close();

}
