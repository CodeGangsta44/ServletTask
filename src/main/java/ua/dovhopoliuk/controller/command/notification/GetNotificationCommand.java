package ua.dovhopoliuk.controller.command.notification;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.notification.get.GetAllNotificationsCommand;
import ua.dovhopoliuk.controller.command.notification.get.GetAllNotificationsOfCurrentUserCommand;
import ua.dovhopoliuk.model.service.NotificationService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class GetNotificationCommand implements Command {
    private Map<String, Command> commands;
    private NotificationService notificationService;

    public GetNotificationCommand(NotificationService notificationService) {
        this.notificationService = notificationService;

        commands = new HashMap<>();

        commands.put("",
                new GetAllNotificationsCommand(notificationService));
        commands.put("me",
                new GetAllNotificationsOfCurrentUserCommand(notificationService));

    }

    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        path = path.replaceFirst(".*/notifications/", "").replaceFirst("\\d+", "id");

        Command command = commands.getOrDefault(path, (e)->"redirect:/");
        return command.execute(request);
    }
}
