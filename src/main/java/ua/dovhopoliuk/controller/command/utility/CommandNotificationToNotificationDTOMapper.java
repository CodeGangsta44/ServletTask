package ua.dovhopoliuk.controller.command.utility;

import ua.dovhopoliuk.model.dto.NotificationDTO;
import ua.dovhopoliuk.model.entity.Notification;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

public class CommandNotificationToNotificationDTOMapper {
    public static NotificationDTO convert(HttpServletRequest request, Notification notification) {
        NotificationDTO dto = new NotificationDTO();

        String topicPattern =  CommandBundleUtility
                .getMessage(request, "message", notification.getTopicKey());

        String messagePattern = CommandBundleUtility
                .getMessage(request, "message",  notification.getMessageKey());

        String topic = MessageFormat.format(topicPattern, notification.getTopicValues().toArray());
        String message = MessageFormat.format(messagePattern, notification.getMessageValues().toArray());

        dto.setId(notification.getId());
        dto.setNotificationDateTime(notification.getNotificationDateTime());
        dto.setTopic(topic);
        dto.setMessage(message);

        return  dto;
    }
}
