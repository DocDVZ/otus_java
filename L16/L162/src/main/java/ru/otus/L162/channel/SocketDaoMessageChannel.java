package ru.otus.L162.channel;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.L162.messaging.EventListener;
import ru.otus.L162.messaging.messages.DaoSocketMessage;
import ru.otus.L162.messaging.messages.SocketMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by DocDVZ on 27.08.2017.
 */
public class SocketDaoMessageChannel implements MessageChannel<DaoSocketMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(SocketDaoMessageChannel.class);
    private static final int WORKERS_COUNT = 2;

    private final BlockingQueue<DaoSocketMessage> output = new LinkedBlockingQueue<>();
    private final BlockingQueue<DaoSocketMessage> input = new LinkedBlockingQueue<>();

    private final ExecutorService executor;
    private final Socket socket;
    private final List<EventListener> shutdownRegistrations;

    public SocketDaoMessageChannel(Socket socket){
        this.socket = socket;
        this.shutdownRegistrations = new CopyOnWriteArrayList<>();
        this.executor = Executors.newFixedThreadPool(WORKERS_COUNT);
    }

    public SocketDaoMessageChannel(String host, int port) throws IOException {
        this(new Socket(host, port));
        init();
    }

    public void init() {
        executor.execute(this::sendMessage);
        executor.execute(this::receiveMessage);
    }

    private void sendMessage() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            while (socket.isConnected()) {
                SocketMessage msg = output.take(); //blocks
                String json = new Gson().toJson(msg);
                LOG.debug("Sending message: " + json);
                out.println(json);
                out.println(); //end of message
            }
        } catch (InterruptedException | IOException e) {
            LOG.error(e.getMessage());
        }
    }

    private void receiveMessage() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                LOG.debug("Message received: " + inputLine);
                stringBuilder.append(inputLine);
                if (inputLine.isEmpty() && !stringBuilder.toString().isEmpty()) {
                    String json = stringBuilder.toString();
                    DaoSocketMessage msg = getMsgFromJSON(json);
                    input.add(msg);
                    stringBuilder = new StringBuilder();
                }
            }
        } catch (IOException | ParseException | ClassNotFoundException e) {
            LOG.error(e.getMessage());
        } finally {
            close();
        }
    }

    private DaoSocketMessage getMsgFromJSON(String json) throws ParseException, ClassNotFoundException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
        String className = (String) jsonObject.get(SocketMessage.CLASS_NAME_VARIABLE);
        Class<?> msgClass = Class.forName(className);
        return (DaoSocketMessage) new Gson().fromJson(json, msgClass);
    }


    @Override
    public void send(DaoSocketMessage message) {
        output.add(message);
    }

    @Override
    public DaoSocketMessage take() throws InterruptedException {
        return input.take();
    }

    @Override
    public DaoSocketMessage poll() {
        return input.poll();
    }

    @Override
    public void close() {
        shutdownRegistrations.forEach(EventListener::onEvent);
        shutdownRegistrations.clear();

        executor.shutdown();
    }
}