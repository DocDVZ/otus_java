package ru.otus.L161.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * Created by DocDVZ on 27.08.2017.
 */
@Component
public class ProcessHelperBean {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessHelperBean.class);
    private static final String MESSAGE_SERVER_START_COMMAND = "java -jar ";
    private static final String MESSAGE_SERVER_NAME = "Message Server";
    private static final String PROPERTY_NAME = "catalina.home";
    private static final String MSG_JAR_NAME = "MessageService.jar";
    private static final String DAO_JAR_NAME = "DaoService.jar";

    private ProcessRunner messsageServerProcess;
    private ProcessRunner daoServiceProcess;





    public void startMessageServer(Integer serverPort){
        try {
            LOG.info("Starting message server process.");
            messsageServerProcess = new ProcessRunnerImpl();
            String command =  MESSAGE_SERVER_START_COMMAND + getServerCommand() + " " + serverPort;
            messsageServerProcess.start(command, MESSAGE_SERVER_NAME);
            LOG.info("Message server started");
        } catch (IOException e) {
            LOG.error("Cannot start message server process. ", e);
        }
    }

    public void startDaoService(Integer serverPort){
        try {
            LOG.info("Starting dao service process.");
            daoServiceProcess = new ProcessRunnerImpl();
            String command =  MESSAGE_SERVER_START_COMMAND + getDaoCommand() + " " + serverPort;
            daoServiceProcess.start(command, MESSAGE_SERVER_NAME);
            LOG.info("Dao service started");
        } catch (IOException e) {
            LOG.error("Cannot start message server process. ", e);
        }
    }



    private String getServerCommand(){
        String path = System.getProperty(PROPERTY_NAME) + "/lib/" + MSG_JAR_NAME;
        return path;
    }

    private String getDaoCommand(){
        String path = System.getProperty(PROPERTY_NAME) + "/lib/" + DAO_JAR_NAME;
        return path;
    }

    @PreDestroy
    public void finish() {
        if (messsageServerProcess != null) {
            messsageServerProcess.stop();
        }
    }

    public boolean isMessageServerStarted() {
        return messsageServerProcess.isStarted();
    }
}
