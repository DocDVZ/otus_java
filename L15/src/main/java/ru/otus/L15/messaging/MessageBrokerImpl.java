package ru.otus.L15.messaging;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.L15.app.MessageBroker;
import ru.otus.L15.messaging.exceptions.WrongAddressException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MessageBrokerImpl implements MessageBroker {

    private final Map<Integer, Request> requestMap = new ConcurrentHashMap<>();
    private final Map<String, MessageConsumer> consumerMap = new HashMap<>();



    @Autowired
    public MessageBrokerImpl(List<MessageConsumer> consumers){
        for (MessageConsumer c : consumers){
            consumerMap.put(c.getConsumerId(), c);
        }
    }


    public RequestResult processRequest(Request request){
        Integer requestID = request.getID();
        requestMap.put(requestID, request);
        MessageConsumer handler =consumerMap.get(request.getConsumerID());
        if (handler==null){
            throw new WrongAddressException("No consumer found for request " + request);
        } else{
            handler.handleRequest(request);
        }
        RequestResult result = request.getResult();
        requestMap.remove(requestID);
        return result;
    }
}