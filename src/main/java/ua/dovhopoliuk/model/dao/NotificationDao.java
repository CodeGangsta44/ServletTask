package ua.dovhopoliuk.model.dao;

import ua.dovhopoliuk.model.entity.Notification;

import java.util.List;

public interface NotificationDao extends GenericDao<Notification> {
    List<Notification> findAllByAddressedUserId(Long id);
}
