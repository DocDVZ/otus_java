package ru.otus.L09.frontend.servlets;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.L09.examples.SimpleEntity;
import ru.otus.L09.orm.OrmTool;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by dzvyagin on 18.07.2017.
 */
public class ORMOperationsServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(ORMOperationsServlet.class);
    private static final String ID_PARAMETER = "intField";


    /**
     * Getting entity
     */
    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter(ID_PARAMETER);

        int responseStatus;
        String message;
        if (id != null) {
            EntityManager em = null;
            try {
                EntityManagerFactory emf = OrmTool.getInstance().getSessionFactory();
                em = emf.createEntityManager();
                SimpleEntity entity = em.find(SimpleEntity.class, Integer.parseInt(id));
                responseStatus = HttpServletResponse.SC_OK;
                message = entity ==  null ? "Cannot find entity by id " + id : entity.toString();
            }catch (Exception e){
                LOG.error("Cannot read entity with id " + id, e);
                responseStatus = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                message = "Cannot read entity with id " + id + ": " + e.getMessage();
            } finally {
                if (em!=null){
                    em.close();
                }
            }
        } else {
            responseStatus = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            message = "id is null or unparsable";
        }

        response.setStatus(responseStatus);
        response.getWriter().println(message);
    }

    /**
     * Creating new entity
     */
    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        StringBuffer sb = new StringBuffer();
        String requestObject = null;
        try {
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line);
            requestObject = sb.toString();
        } catch (Exception e) {
            LOG.error("Cannot read request data", e);
        }

        LOG.debug("Incoming json " + requestObject);

        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateDeserializer()).create();
        SimpleEntity entity = gson.fromJson(requestObject, SimpleEntity.class);
        EntityManager em = null;
        try {
            EntityManagerFactory emf = OrmTool.getInstance().getSessionFactory();
            em = emf.createEntityManager();
            em.persist(entity);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(entity);
        }catch (Exception e){
            LOG.error("Cannot save entity " + entity, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println(e.getMessage());
        } finally {
            if (em!=null){
                em.close();
            }
        }
    }

    private class DateDeserializer implements JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
            String date = element.getAsString();
            if (date.isEmpty()){return null;}
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            format.setTimeZone(TimeZone.getTimeZone("GMT+3"));

            try {
                return format.parse(date);
            } catch (ParseException exp) {
                LOG.error("Failed to parse Date:", exp);
                return null;
            }
        }
    }

}
