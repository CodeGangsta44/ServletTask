package ua.dovhopoliuk.model.service;


import ua.dovhopoliuk.model.dao.DaoFactory;
import ua.dovhopoliuk.model.dao.NotificationDao;
import ua.dovhopoliuk.model.entity.Notification;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class NotificationService {
    private final DaoFactory daoFactory = DaoFactory.getInstance();
    private UserService userService;

    public NotificationService(UserService userService) {
        this.userService = userService;
    }

    public List<Notification> getAllNotifications(){
        try (NotificationDao notificationDao = daoFactory.createNotificationDao()) {
            return notificationDao.findAll();
        }
    }

    public List<Notification> getCurrentUserNotifications(HttpServletRequest request) {
        try (NotificationDao notificationDao = daoFactory.createNotificationDao()) {
            return notificationDao.findAllByAddressedUserId(userService.getIdOfCurrentUser(request));
        }
    }

    public void deleteNotification(Long notificationId) {
        try (NotificationDao notificationDao = daoFactory.createNotificationDao()) {
            notificationDao.delete(notificationId);
        }
    }
}
