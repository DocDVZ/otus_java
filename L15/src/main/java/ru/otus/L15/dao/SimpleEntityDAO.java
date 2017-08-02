package ru.otus.L15.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.L15.app.EntityDao;
import ru.otus.L15.examples.SimpleEntity;
import ru.otus.L15.messaging.*;
import ru.otus.L15.messaging.exceptions.WrongAddressException;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by DocDVZ on 01.08.2017.
 */
@Component
@Qualifier("SimpleEntityDAO")
public class SimpleEntityDAO implements EntityDao<SimpleEntity>, MessageConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleEntityDAO.class);
    private static final String CONSUMER_ID = ConsumersContext.SIMPLE_ENTITY_DAO_ID;
    private final ConcurrentLinkedQueue<DAORequest> requestQueue = new ConcurrentLinkedQueue<>();
    private static final int DEFAULT_STEP_TIME = 10;

    @PostConstruct
    public void init() {
        Thread worker = new Thread(new DaoWorker());
        worker.setName("dao-worker");
        worker.start();
    }

    @Autowired
    private EntityManagerFactory emf;

    @Override
    public SimpleEntity getByID(Integer id) {
        SimpleEntity se = null;
        EntityManager em = null;

        try {
            em = emf.createEntityManager();
            se = em.find(SimpleEntity.class, id);
        } catch (Exception e) {
            LOG.error("Cannot find SimpleEntity by id " + id, e);
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return se;
    }


    @Override
    public void save(SimpleEntity entity) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.persist(entity);
        } catch (Exception e) {
            LOG.error("Cannot save SimpleEntity " + entity, e);
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public SimpleEntity merge(SimpleEntity entity) {
        EntityManager em = null;

        try {
            em = emf.createEntityManager();
            entity = em.merge(entity);
        } catch (Exception e) {
            LOG.error("Cannot merge SimpleEntity " + entity, e);
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return entity;
    }

    @Override
    public String getConsumerId() {
        return CONSUMER_ID;
    }

    @Override
    public void handleRequest(Request request) {
        if (request instanceof DAORequest) {
            requestQueue.add((DAORequest) request);
        } else {
            throw new WrongAddressException("Wrong request type came to process, got: " + request.getClass() + ", expected:" + DAORequest.class);
        }
    }

    private class DaoWorker implements Runnable {

        @SuppressWarnings("InfiniteLoopStatement")
        public void run() {
            while (true) {
                while (!requestQueue.isEmpty()) {
                    DAORequest request = requestQueue.poll();
                    RequestResult result = new RequestResult();
                    SimpleEntity se = null;
                    try {
                        switch (request.getCommand()) {
                            case READ:
                                se = getByID((Integer) request.getPayload());
                                result.setSuccess(true);
                                result.setPayload(se);
                                request.setResult(result);
                                break;
                            case CREATE:
                                save((SimpleEntity) request.getPayload());
                                result.setSuccess(true);
                                request.setResult(result);
                                break;
                            case MERGE:
                                se = merge((SimpleEntity) request.getPayload());
                                result.setSuccess(true);
                                result.setPayload(se);
                                request.setResult(result);
                                break;
                        }
                    } catch (Exception e){
                        result.setSuccess(false);
                        request.setResult(result);
                    }

                }
                try {
                    Thread.sleep(DEFAULT_STEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
