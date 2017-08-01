package ru.otus.L15.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/ajax")
public class AjaxController {

    private static Logger LOG = LoggerFactory.getLogger(AjaxController.class);

    @RequestMapping(value = "/beans", method = RequestMethod.GET)
    public Set<MBeanWrapper> getBeans() {
        try {
            LOG.trace("AjaxController: /ajax/beans");
            Set<MBeanWrapper> result = spillTheBeans();
            return result;
        } catch (Exception e) {
            LOG.error("Cannot get beans for ajax request", e);
            return null;
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
