package ru.otus.L09.orm;

import javax.persistence.*;
import java.sql.Statement;

/**
 * Created by dzvyagin on 15.06.2017.
 */
public class OrmSession implements EntityManager {

    private Statement statement;
    private boolean isOpen = false;

    public OrmSession(Statement statement){
        this.statement = statement;
    }


    @Override
    public void persist(Object o) {

    }

    @Override
    public <T> T merge(T t) {
        return null;
    }

    @Override
    public void remove(Object o) {

    }

    @Override
    public <T> T find(Class<T> aClass, Object o) {
        return null;
    }

    @Override
    public <T> T getReference(Class<T> aClass, Object o) {
        return null;
    }

    @Override
    public void flush() {

    }

    @Override
    public void setFlushMode(FlushModeType flushModeType) {

    }

    @Override
    public FlushModeType getFlushMode() {
        return null;
    }

    @Override
    public void lock(Object o, LockModeType lockModeType) {

    }

    @Override
    public void refresh(Object o) {

    }

    @Override
    public void clear() {

    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Query createQuery(String s) {
        return null;
    }

    @Override
    public Query createNamedQuery(String s) {
        return null;
    }

    @Override
    public Query createNativeQuery(String s) {
        return null;
    }

    @Override
    public Query createNativeQuery(String s, Class aClass) {
        return null;
    }

    @Override
    public Query createNativeQuery(String s, String s1) {
        return null;
    }

    @Override
    public void joinTransaction() {

    }

    @Override
    public Object getDelegate() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public EntityTransaction getTransaction() {
        return null;
    }
}
