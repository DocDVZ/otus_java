package ru.otus.L161.messaging;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.L161.process.ProcessRunner;
import ru.otus.L161.process.ProcessRunnerImpl;
import ru.otus.L162.channel.MessageChannel;
import ru.otus.L162.channel.SocketDaoMessageChannel;
import ru.otus.L162.client.ClientMessageHanler;
import ru.otus.L162.messaging.Addressee;
import ru.otus.L162.messaging.messages.DaoSocketMessage;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * Created by DocDVZ on 27.08.2017.
 */
@Component
public class DaoMessageHandler implements ClientMessageHanler<DaoSocketMessage> {

    private static final String MESSAGE_SERVER_START_COMMAND = "java -jar ../L162/target/MessageServer.jar";
    private static final String MESSAGE_SERVER_NAME = "Message Server";
    private static final Integer DEFAULT_RESPONSE_TIMEOUT_MS = 30_000;
    private static final Logger LOG = LoggerFactory.getLogger(DaoMessageHandler.class);

    private final Map<String, RequestResponsePair> messages = new ConcurrentHashMap<>();

    private MessageChannel<DaoSocketMessage> channel;
    private ProcessRunner messsageServerProcess;
//    private

    @Value("${messaging.socket.startothernodes ?: true}")
    private Boolean startOtherNodes;
    @Value("${messaging.socket.messageserver.host ?:localhost}")
    private String serverHost;
    @Value("${messaging.socket.messageserver.port ?: 5050}")
    private Integer serverPort;


    @PostConstruct
    public void init() throws Exception{
//        if (startOtherNodes) {
//            Thread messsageServerThread = new Thread(() -> {
//                try {
//                    LOG.info("Starting message server process.");
//                    messsageServerProcess = new ProcessRunnerImpl();
//                    messsageServerProcess.start(MESSAGE_SERVER_START_COMMAND, MESSAGE_SERVER_NAME);
//                    LOG.info("Message server started.");
//                } catch (IOException e) {
//                    LOG.error("Cannot start message server process. ", e);
//                }
//            });
//            messsageServerThread.setName("message-server-tread");
//        }
//        Thread.sleep(10000);
        channel = new SocketDaoMessageChannel(serverHost, serverPort);
    }

    @PreDestroy
    public void finish() {
        if (messsageServerProcess != null) {
            messsageServerProcess.stop();
        }
    }

    @Override
    public String send(DaoSocketMessage daoSocketMessage) {
        String id = UUID.randomUUID().toString();
        daoSocketMessage.setCorrelationID(id);
        channel.send(daoSocketMessage);
        messages.put(id, new RequestResponsePair(id, daoSocketMessage));
        return id;
    }

    @Override
    public DaoSocketMessage getResponse(String id) throws TimeoutException {
        RequestResponsePair pair = messages.get(id);
        if (pair==null){
            LOG.warn("No message found with id " + id);
            return null;
        }
        DaoSocketMessage response = pair.getResponse();
        messages.remove(id);
        return response;
    }

    private class RequestResponsePair{

        private String id;
        private DaoSocketMessage request;
        private DaoSocketMessage response;
        private volatile boolean isReady = false;
        private Object lock;

        public RequestResponsePair(String id, DaoSocketMessage request) {
            this.id = id;
            this.request = request;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public DaoSocketMessage getRequest() {
            return request;
        }

        public void setRequest(DaoSocketMessage request) {
            this.request = request;
        }

        public DaoSocketMessage getResponse() throws TimeoutException {
            synchronized (lock) {
                if (!isReady) {
                    try {
                        lock.wait(DEFAULT_RESPONSE_TIMEOUT_MS);
                        if (response == null){
                            throw new TimeoutException();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return response;
        }

        public void setResponse(DaoSocketMessage response) {
            synchronized (lock) {
                isReady = true;
                lock.notify();
            }
            this.response = response;
        }
    }
}
