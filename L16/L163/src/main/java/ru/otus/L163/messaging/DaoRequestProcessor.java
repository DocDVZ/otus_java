package ru.otus.L163.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.L162.messaging.Addressee;
import ru.otus.L162.messaging.messages.DaoSocketMessage;
import ru.otus.L162.model.SimpleEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Created by DocDVZ on 29.08.2017.
 */
public class DaoRequestProcessor implements RequestProcessor<DaoSocketMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(DaoRequestProcessor.class);

    public EntityManagerFactory emf;


    public DaoRequestProcessor(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public DaoSocketMessage process(DaoSocketMessage request) {
        LOG.debug("Processing persistence request " + request);
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            SimpleEntity entity = request.getRequestData();
            if (entity != null) {
                SimpleEntity responseData = null;
                switch (request.getOperation()) {
                    case CREATE:
                        em.persist(entity);
                        responseData = entity;
                        break;
                    case READ:
                        responseData = em.find(SimpleEntity.class, entity.getBigIntegerField());
                        break;
                    case MERGE:
                        responseData = em.merge(entity);
                        break;
                }
                request.setResponseData(responseData);
                request.setSuccess(true);
            } else {
                request.setSuccess(false);
            }
        } catch (Exception e) {
            LOG.error("Cannot process socket message", e);
            request.setSuccess(false);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
            Addressee from = request.getFrom();
            Addressee to = request.getTo();
            request.setTo(from);
            request.setFrom(to);
        }

        return request;
    }


}
