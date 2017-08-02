package ru.otus.L15.app;

import ru.otus.L15.messaging.Request;
import ru.otus.L15.messaging.RequestResult;

public interface MessageBroker {

    RequestResult processRequest(Request request);

}
