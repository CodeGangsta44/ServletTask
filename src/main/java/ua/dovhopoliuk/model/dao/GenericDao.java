package ua.dovhopoliuk.model.dao;

import java.util.List;

public interface GenericDao<T> extends AutoCloseable {
    void create(T entity);
    T findById(Long id);
    T findByLogin(String login);
    List<T> findAll();
    void update(T entity);
    void delete(Long id);
}
