package ua.dovhopoliuk.controller.command.notification;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.model.service.NotificationService;

import javax.management.Notification;
import javax.servlet.http.HttpServletRequest;

public class DeleteNotificationByIdCommand implements Command {
    private NotificationService notificationService;

    public DeleteNotificationByIdCommand(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        Long notificationId = Long.parseLong(path.replaceFirst(".*/notifications/", ""));

        notificationService.deleteNotification(notificationId);

        return null;
    }
}

