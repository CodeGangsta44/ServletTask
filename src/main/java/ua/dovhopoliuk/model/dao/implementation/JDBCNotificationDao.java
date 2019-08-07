package ua.dovhopoliuk.model.dao.implementation;

import ua.dovhopoliuk.model.dao.NotificationDao;
import ua.dovhopoliuk.model.entity.Notification;

import java.sql.Connection;
import java.util.List;

public class JDBCNotificationDao implements NotificationDao {
    private final Connection connection;

    JDBCNotificationDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Notification entity) {

    }

    @Override
    public Notification findById(Long id) {
        return null;
    }

    @Override
    public List<Notification> findAll() {
        return null;
    }

    @Override
    public void update(Notification entity) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void close() throws Exception {

    }
}
