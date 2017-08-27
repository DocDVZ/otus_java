package ru.otus.L162;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by DocDVZ on 11.08.2017.
 */
public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class.getName());

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length == 0 || args[0] == null){
            LOG.warn("No port defined or arg is invalid num, starting default 5050");
            port = 5050;
        } else {
            port = Integer.parseInt(args[0]);
        }
        BlockingServer server = new BlockingServer(port);
        server.start();
    }

    private static Boolean isValidPort(String port){
        try {
            Integer p = Integer.parseInt(port);
            return p > 0 && p < 65535;
        } catch (NumberFormatException e){
            return false;
        }
    }
}
