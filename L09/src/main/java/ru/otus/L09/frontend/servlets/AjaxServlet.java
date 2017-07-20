package ru.otus.L09.frontend.servlets;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.L09.frontend.PageGenerator;

import javax.management.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by DocDVZ on 14.07.2017.
 */
public class AjaxServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(AjaxServlet.class);

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            Map<String, Object> pageVariables = new HashMap<>();
            MBeanServerConnection mbs = ManagementFactory.getPlatformMBeanServer();
            Set<ObjectInstance> mBeans = mbs.queryMBeans(null, null);
            Gson gson = new Gson();
            response.getWriter().println(gson.toJson(spillTheBeans()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Set<MBeanWrapper> spillTheBeans() throws Exception {
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
                        wa.setValue(null);
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


    private class MBeanWrapper{

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

    private class WrapperAttribute{
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
