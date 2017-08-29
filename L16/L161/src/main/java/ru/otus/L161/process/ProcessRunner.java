package ru.otus.L161.process;

import java.io.IOException;

/**
 * Created by DocDVZ on 27.08.2017.
 */
public interface ProcessRunner {
    void start(String command, String processID) throws IOException;

    void stop();

    boolean isStarted();
}
