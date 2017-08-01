package ru.otus.L15.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.L15.app.EntityDao;
import ru.otus.L15.examples.SimpleEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Created by DocDVZ on 01.08.2017.
 */
@Component
@Qualifier("SimpleEntityDAO")
public class SimpleEntityDAO implements EntityDao<SimpleEntity> {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleEntityDAO.class);

    @Autowired
    private EntityManagerFactory emf;

    @Override
    public SimpleEntity getByID(Integer id) {
        SimpleEntity se = null;
        EntityManager em = null;

        try {
            em = emf.createEntityManager();
            se = em.find(SimpleEntity.class, id);
        } catch (Exception e){
             LOG.error("Cannot find SimpleEntity by id " + id, e);
             throw e;
        } finally {
            if (em!=null && em.isOpen()){
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
        } catch (Exception e){
            LOG.error("Cannot save SimpleEntity " + entity, e);
            throw e;
        } finally {
            if (em!=null && em.isOpen()){
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
        } catch (Exception e){
            LOG.error("Cannot merge SimpleEntity " + entity, e);
            throw e;
        } finally {
            if (em!=null && em.isOpen()){
                em.close();
            }
        }

        return entity;
    }
}
