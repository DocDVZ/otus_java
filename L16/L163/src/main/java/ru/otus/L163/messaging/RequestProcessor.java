package ru.otus.L163.messaging;

import ru.otus.L162.messaging.messages.SocketMessage;

/**
 * Created by DocDVZ on 29.08.2017.
 */
public interface RequestProcessor<T extends SocketMessage> {

    T process(T request);

}
