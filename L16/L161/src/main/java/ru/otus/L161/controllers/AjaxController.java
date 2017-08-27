package ru.otus.L161.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.L161.messaging.MessagingContext;
import ru.otus.L162.client.ClientMessageHanler;
import ru.otus.L162.messaging.CrudOperation;
import ru.otus.L162.messaging.messages.DaoSocketMessage;
import ru.otus.L162.model.SimpleEntity;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/ajax")
public class AjaxController {

    @Autowired
    private ClientMessageHanler<DaoSocketMessage> messageHandler;

    private static Logger LOG = LoggerFactory.getLogger(AjaxController.class);

    @RequestMapping(value = "/beans", method = RequestMethod.GET)
    public Set<MBeanWrapper> getBeans() {
        try {
            LOG.debug("AjaxController: /ajax/beans");
            Set<MBeanWrapper> result = spillTheBeans();
            return result;
        } catch (Exception e) {
            LOG.error("Cannot get beans for ajax request", e);
            return null;
        }
    }

    @RequestMapping(value =  "/ormOperations", method = RequestMethod.GET)
    public SimpleEntity getEntity(@RequestParam(name = "intField") Integer id){
        LOG.debug("AjaxController: /ajax/ormOperations getting entity by id " + id);
        DaoSocketMessage request = new DaoSocketMessage(MessagingContext.MY_ADDRESS, MessagingContext.DAO_ADDRESS);
        SimpleEntity requestData = new SimpleEntity();
        requestData.setIntField(id);
        request.setRequestData(requestData);
        request.setOperation(CrudOperation.READ);
        String correlationID = messageHandler.send(request);
        SimpleEntity entity = null;
        try {
            entity = messageHandler.getResponse(correlationID).getResponseData();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @RequestMapping(value =  "/ormOperations", method = RequestMethod.POST)
    public ResponseEntity createEntity(@RequestBody SimpleEntity entity){
        LOG.debug("AjaxController: /ajax/ormOperations updating entity " + entity);
        if (entity==null){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        DaoSocketMessage request = new DaoSocketMessage(MessagingContext.MY_ADDRESS, MessagingContext.DAO_ADDRESS);
        SimpleEntity requestData = new SimpleEntity();
        request.setRequestData(entity);
        request.setOperation(CrudOperation.CREATE);
        String correlationID = messageHandler.send(request);
        DaoSocketMessage response = null;
        try {
            response = messageHandler.getResponse(correlationID);
        } catch (TimeoutException e) {
            LOG.error("Cannot create entity, timedout waiting");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (response.getSuccess()){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



    private Set<MBeanWrapper> spillTheBeans(/*final Writer out*/) throws Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();

        final Set<ObjectName> mbeans = new HashSet<ObjectName>();
        mbeans.addAll(server.queryNames(null, null));
        Set<MBeanWrapper> beans = new HashSet<>();
        for (final ObjectName mbean : mbeans) {
            MBeanWrapper mBeanWrapper = new MBeanWrapper();
            mBeanWrapper.setName(mbean.toString());

            Set<WrapperAttribute> attrs = new HashSet<>();
            final MBeanAttributeInfo[] attributes = server.getMBeanInfo(
                    mbean).getAttributes();
            for (final MBeanAttributeInfo attribute : attributes) {
                WrapperAttribute wa = new WrapperAttribute();
                wa.setName(attribute.getName());
                wa.setType(attribute.getType());

                try {
                    final Object value = server.getAttribute(mbean,
                            attribute.getName());
                    if (value == null) {

                    } else {
                        wa.setValue(value.toString());
                    }
                } catch (Exception e) {
                    wa.setError(e.getMessage());
                }
                attrs.add(wa);
            }
            mBeanWrapper.setAttributes(attrs);
            beans.add(mBeanWrapper);
        }
        return beans;
    }


    private class MBeanWrapper {

        private String name;
        private Set<WrapperAttribute> attributes;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Set<WrapperAttribute> getAttributes() {
            return attributes;
        }

        public void setAttributes(Set<WrapperAttribute> attributes) {
            this.attributes = attributes;
        }
    }

    private class WrapperAttribute {
        private String name;
        private String value;
        private String type;
        private String error;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
