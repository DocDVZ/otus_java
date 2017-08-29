package ru.otus.L161.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by tully.
 */
public class ProcessRunnerImpl implements ProcessRunner{

    private Process process;
    private String id;
    private volatile boolean isStarted = false;


    public void start(String command, String processID) throws IOException {
        this.process = runProcess(command);
        this.id = processID;
    }

    public void stop() {
        process.destroy();
    }


    private Process runProcess(String command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command.split(" "));
        pb.redirectErrorStream(true);
        Process p = pb.start();

        StreamListener errors = new StreamListener(p.getErrorStream(), "ERROR");
        StreamListener output = new StreamListener(p.getInputStream(), "OUTPUT");

        output.start();
        errors.start();
        return p;
    }

    private class StreamListener extends Thread {
        private final Logger LOG = LoggerFactory.getLogger(StreamListener.class);

        private final InputStream is;
        private final String type;

        private StreamListener(InputStream is, String type) {
            this.is = is;
            this.type = type;
        }

        @Override
        public void run() {
            try (InputStreamReader isr = new InputStreamReader(is)) {
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    LOG.debug(String.format("Process %s - %s - %s", id, type, line));
                    if (line.contains("Server started")){
                        isStarted = true;
                    }
                }
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }
    }

    @Override
    public boolean isStarted(){
        return isStarted;
    }
}
