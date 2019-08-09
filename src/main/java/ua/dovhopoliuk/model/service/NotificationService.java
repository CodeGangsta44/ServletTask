package ua.dovhopoliuk.model.service;


import ua.dovhopoliuk.model.dao.DaoFactory;
import ua.dovhopoliuk.model.entity.Notification;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationService {
    private final DaoFactory daoFactory = DaoFactory.getInstance();
    private UserService userService;

    public NotificationService(UserService userService) {
        this.userService = userService;
    }

    public List<Notification> getAllNotifications(){
        return daoFactory.createNotificationDao().findAll();
    }

    public List<Notification> getCurrentUserNotifications(HttpServletRequest request) {
        //TODO: implement this method
        return daoFactory.createNotificationDao().findAll().stream()
                .filter(notification -> {
                    return notification.getAddressedUser().equals(userService.getCurrentUser(request));
                })
                .collect(Collectors.toList());
    }

    public void deleteNotification(Notification notification) {
        daoFactory.createNotificationDao().delete(notification.getId());
    }
}
