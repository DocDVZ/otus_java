package ru.otus.L162;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.L162.channel.SocketDaoMessageChannel;
import ru.otus.L162.messaging.Addressee;
import ru.otus.L162.messaging.messages.SocketMessage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Created by DocDVZ on 27.08.2017.
 */
public class BlockingServer {
    private static final Logger logger = LoggerFactory.getLogger(BlockingServer.class.getName());

    private static final int THREADS_NUMBER = 1;
    private static final int CHANNEL_READING_DELAY = 20;
    private final int PORT;

    private final ExecutorService executor;
    private final Map<Addressee, ru.otus.L162.channel.MessageChannel> channels;
    private final List<ru.otus.L162.channel.MessageChannel> undefinedChannels;
    private final Map<Addressee, Queue<SocketMessage>> unreadedMessages;


    public BlockingServer(int port) {
        this.PORT = port;
        executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        channels = new ConcurrentHashMap<>();
        undefinedChannels = new CopyOnWriteArrayList<>();
        unreadedMessages = new ConcurrentHashMap<>();
    }

    public void start() throws Exception {
        executor.submit(this::process);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Server started on port: " + serverSocket.getLocalPort());
            while (!executor.isShutdown()) {
                Socket client = serverSocket.accept(); //blocks
                SocketDaoMessageChannel channel = new SocketDaoMessageChannel(client);
                channel.init();
                undefinedChannels.add(channel);
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void process() {
        while (true) {
                for (ru.otus.L162.channel.MessageChannel channel : undefinedChannels) {
                    SocketMessage msg = channel.poll(); //get
                    if (msg != null) {
                        Addressee from = msg.getFrom();
                        Addressee to = msg.getTo();
                        if (to == null){
                            logger.warn("Message from unknown channel, dropping message: " + msg);
                            continue;
                        }
                        undefinedChannels.remove(channel);
                        channels.put(from, channel);
                        logger.info("Registered new addressed channel: " + to.getName());
                        if (to != null) {
                            sendMessage(msg);
                        }
                        checkUnreadedMessages(from);
                    }
                }
                for (ru.otus.L162.channel.MessageChannel channel : channels.values()) {
                    SocketMessage msg = channel.poll(); //get
                    if (msg != null) {
                        Addressee to = msg.getTo();
                        if (to != null) {
                            sendMessage(msg);
                        }
                    }
                }
            try {
                Thread.sleep(CHANNEL_READING_DELAY);
            } catch (InterruptedException e) {
                logger.error(e.toString());
            }
        }
    }

    private void sendMessage(SocketMessage message) {
        Addressee to = message.getTo();
        ru.otus.L162.channel.MessageChannel channel = channels.get(to);
        if (channel == null) {
            Queue<SocketMessage> queue = unreadedMessages.get(to);
            if (queue == null) {
                queue = new LinkedBlockingQueue<>();
            }
            queue.add(message);
        } else {
            channel.send(message);
        }
    }

    private void checkUnreadedMessages(Addressee addr) {
        Queue<SocketMessage> queue = unreadedMessages.remove(addr);
        if (queue != null) {
            while (true) {
                SocketMessage message = queue.poll();
                if (message == null) break;
                sendMessage(message);
            }
        }
    }
}
