package ru.otus.L15.messaging;

import java.util.function.Consumer;

public interface MessageConsumer {

    String getConsumerId();
    void handleRequest(Request request);

}
