package ru.otus.L15.app;

/**
 * Created by DocDVZ on 01.08.2017.
 */
public interface EntityDao<T> {

    public T getByID(Integer id);

    public void save(T entity);

    public T merge(T entity);

}
