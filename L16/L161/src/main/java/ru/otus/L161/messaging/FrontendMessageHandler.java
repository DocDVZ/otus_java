package ru.otus.L161.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.L161.process.ProcessHelperBean;
import ru.otus.L162.channel.MessageChannel;
import ru.otus.L162.channel.SocketDaoMessageChannel;
import ru.otus.L162.client.ClientMessageHanler;
import ru.otus.L162.messaging.messages.DaoSocketMessage;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * Created by DocDVZ on 27.08.2017.
 */
@Component
public class FrontendMessageHandler implements ClientMessageHanler<DaoSocketMessage> {


    private static final Integer DEFAULT_RESPONSE_TIMEOUT_MS = 30_000;
    private static final Logger LOG = LoggerFactory.getLogger(FrontendMessageHandler.class);

    private final Map<String, RequestResponsePair> messages = new ConcurrentHashMap<>();

    private MessageChannel<DaoSocketMessage> channel;
    private Thread reader;

    @Autowired
    private ProcessHelperBean processHelperBean;

    @Value("${messaging.socket.startothernodes ?: true}")
    private Boolean startOtherNodes;
    @Value("${messaging.socket.messageserver.host ?:localhost}")
    private String serverHost;
    @Value("${messaging.socket.messageserver.port ?: 5050}")
    private Integer serverPort;

    private static volatile Boolean isInited = false;


    @PostConstruct
    public void init() throws Exception {
        if (!isInited) {
            isInited = true;
            if (startOtherNodes) {
                processHelperBean.startMessageServer(serverPort);
                while (!processHelperBean.isMessageServerStarted()) {
                    Thread.sleep(1000);
                }
                LOG.info("Message service is started. Starting clients.");
                processHelperBean.startDaoService(serverPort);
            }

        }

        channel = new SocketDaoMessageChannel(serverHost, serverPort);
        reader = new Thread(() ->{
            while (true){
                try {
                    DaoSocketMessage msg = channel.take();
                    String correlationId = msg.getCorrelationID();
                    RequestResponsePair pair = messages.remove(correlationId);
                    pair.setResponse(msg);
                } catch (Exception e){
                    LOG.error("Cannot read incoming message", e);
                }

            }
        });
        reader.setName("message-handler-reader");
        reader.setDaemon(false);
        reader.start();
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
        if (pair == null) {
            LOG.warn("No message found with id " + id);
            return null;
        }
        DaoSocketMessage response = pair.getResponse();
        messages.remove(id);
        return response;
    }

    private class RequestResponsePair {

        private String id;
        private DaoSocketMessage request;
        private DaoSocketMessage response;
        private volatile boolean isReady = false;
        private Object lock = new Object();

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
                        if (response == null) {
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
