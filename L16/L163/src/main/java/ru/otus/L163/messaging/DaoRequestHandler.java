package ru.otus.L163.messaging;

import ru.otus.L162.channel.MessageChannel;
import ru.otus.L162.messaging.messages.DaoSocketMessage;
import ru.otus.L162.messaging.messages.HelloSocketMessage;
import ru.otus.L162.messaging.messages.SocketMessage;

/**
 * Created by DocDVZ on 29.08.2017.
 */
public class DaoRequestHandler {

    private RequestProcessor<DaoSocketMessage> processor;
    private MessageChannel<DaoSocketMessage> channel;

    private Thread worker;
    private volatile boolean initialized = false;


    public DaoRequestHandler(RequestProcessor<DaoSocketMessage> processor, MessageChannel<DaoSocketMessage> channel) {
        this.processor = processor;
        this.channel = channel;
        registerDaoService();
    }

    private void registerDaoService() {
        DaoSocketMessage msg = new DaoSocketMessage(MessagingContext.DAO_ADDRESS, null);
        channel.send(msg);
    }


    public void start() {
        if (!initialized) {
            worker = new Thread(() -> {
                while (true) {
                    try {
                        DaoSocketMessage message = channel.take();
                        channel.send(processor.process(message));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
            worker.setDaemon(true);
            worker.setName("request-handler-worker");
            worker.start();
            initialized = true;
        }
    }


}
