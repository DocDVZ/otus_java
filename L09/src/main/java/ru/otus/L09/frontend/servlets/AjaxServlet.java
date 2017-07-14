package ru.otus.L09.frontend.servlets;

import com.google.gson.Gson;
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

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            Map<String, Object> pageVariables = new HashMap<>();
            MBeanServerConnection mbs = ManagementFactory.getPlatformMBeanServer();
            Set<ObjectInstance> mBeans = mbs.queryMBeans(null, null);
            Gson gson = new Gson();
//            .println(gson.toJson(mBeans));
            spillTheBeans(response.getWriter());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void spillTheBeans(final Writer out) throws Exception {
        final PrintWriter pw = new PrintWriter(out);
        StringBuilder sb = new StringBuilder("");
        sb.append("<table>");
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();

        sb.append("  <tr><td colspan='4'>&nbsp;</td></tr>");
        sb.append("  <tr><td>Server:</td><td colspan='3'>"
                + server.getClass().getName() + "</td></tr>");

        final Set<ObjectName> mbeans = new HashSet<ObjectName>();
        mbeans.addAll(server.queryNames(null, null));
        for (final ObjectName mbean : mbeans) {
            sb.append("  <tr><td colspan='4'>&nbsp;</td></tr>");
            sb.append("  <tr><td>MBean:</td><td colspan='3'>" + mbean
                    + "</td></tr>");

            final MBeanAttributeInfo[] attributes = server.getMBeanInfo(
                    mbean).getAttributes();
            for (final MBeanAttributeInfo attribute : attributes) {
                sb.append("  <tr><td>&nbsp;</td><td>" + attribute.getName()
                        + "</td><td>" + attribute.getType() + "</td><td>");

                try {
                    final Object value = server.getAttribute(mbean,
                            attribute.getName());
                    if (value == null) {
                        sb.append("<font color='#660000'>null</font>");
                    } else {
                        sb.append(value.toString());
                    }
                } catch (Exception e) {
                    sb.append("<font color='#990000'>" + e.getMessage()
                            + "</font>");
                }

                sb.append("</td></tr>");
            }
        }
        sb.append("</table>");
        pw.println(sb.toString());
        pw.flush();
    }
}
