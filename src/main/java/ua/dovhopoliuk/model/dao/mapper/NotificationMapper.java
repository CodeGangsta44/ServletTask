package ua.dovhopoliuk.model.dao.mapper;

import ua.dovhopoliuk.model.entity.Notification;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class NotificationMapper implements ObjectMapper<Notification> {
    @Override
    public Notification extractFromResultSet(ResultSet resultSet) throws SQLException {
        Notification notification = new Notification();

        notification.setId(resultSet.getLong("notification_id"));
        notification.setMessageKey(resultSet.getString("message_key"));
        notification.setTopicKey(resultSet.getString("topic_key"));
        notification.setNotificationDateTime(resultSet.getTimestamp("notification_date_time").toLocalDateTime());

        return notification;
    }

    @Override
    public Notification makeUnique(Map<Long, Notification> cache, Notification entity) {
        cache.putIfAbsent(entity.getId(), entity);
        return cache.get(entity.getId());
    }

    public String extractMessageValueFromResultSet(ResultSet resultSet) throws SQLException {
        return resultSet.getString("message_values");
    }

    public String extractTopicValueFromResultSet(ResultSet resultSet) throws SQLException {
        return resultSet.getString("topic_values");
    }
}
