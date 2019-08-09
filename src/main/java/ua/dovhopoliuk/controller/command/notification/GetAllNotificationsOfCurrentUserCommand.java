package ua.dovhopoliuk.controller.command.notification;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.controller.command.utility.CommandNotificationToNotificationDTOMapper;
import ua.dovhopoliuk.model.dto.NotificationDTO;
import ua.dovhopoliuk.model.service.NotificationService;

import javax.servlet.http.HttpServletRequest;

public class GetAllNotificationsOfCurrentUserCommand implements Command {
    private CommandJsonUtility<NotificationDTO[]> notificationDTOArrayCommandJsonUtility =
            new CommandJsonUtility<>(NotificationDTO[].class);

    private NotificationService notificationService;

    public GetAllNotificationsOfCurrentUserCommand(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    @Override
    public String execute(HttpServletRequest request) {
        NotificationDTO[] notifications = notificationService.getCurrentUserNotifications(request).stream()
                .map(notification -> CommandNotificationToNotificationDTOMapper.convert(request, notification))
                .toArray(NotificationDTO[]::new);

        return notificationDTOArrayCommandJsonUtility.toJson(notifications);
    }
}
