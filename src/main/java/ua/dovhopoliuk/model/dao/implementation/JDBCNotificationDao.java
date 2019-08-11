package ua.dovhopoliuk.model.dao.implementation;

import ua.dovhopoliuk.model.dao.DaoFactory;
import ua.dovhopoliuk.model.dao.NotificationDao;
import ua.dovhopoliuk.model.dao.UserDao;
import ua.dovhopoliuk.model.dao.mapper.NotificationMapper;
import ua.dovhopoliuk.model.dao.mapper.UserMapper;
import ua.dovhopoliuk.model.entity.Notification;
import ua.dovhopoliuk.model.entity.Role;
import ua.dovhopoliuk.model.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCNotificationDao implements NotificationDao {
    private final Connection connection;

    private static final String SQL_INSERT_NOTIFICATION = "INSERT INTO notifications" +
            "(message_key, " +
            "topic_key, " +
            "notification_date_time, " +
            "addressed_user_user_id) " +
            "VALUES(?,?,?,?)";

    private static final String SQL_INSERT_MESSAGE_VALUES = "INSERT INTO notification_message_values" +
            "(notification_notification_id, " +
            "message_values) " +
            "VALUES (?,?)";

    private static final String SQL_INSERT_TOPIC_VALUES = "INSERT INTO notification_topic_values" +
            "(notification_notification_id, " +
            "topic_values) " +
            "VALUES (?,?)";

    private static final String SQL_SELECT_ALL_NOTIFICATIONS = "SELECT * FROM notifications AS n " +
            "JOIN notification_message_values AS nmv " +
            "ON n.notification_id = nmv.notification_notification_id " +
            "JOIN notification_topic_values AS ntv " +
            "ON n.notification_id = ntv.notification_notification_id " +
            "JOIN users AS u " +
            "ON n.addressed_user_user_id = u.user_id " +
            "JOIN user_roles AS ur " +
            "ON u.user_id = ur.user_user_id";

//    private static final String SQL_SELECT_NOTIFICATION_BY_ID = "SELECT * FROM notifications AS n " +
//            "JOIN notification_message_values AS nmv " +
//            "ON n.notification_id = nmv.notification_notification_id " +
//            "JOIN notification_topic_values AS ntv " +
//            "ON n.notification_id = ntv.notification_notification_id " +
//            "JOIN users AS u " +
//            "ON n.addressed_user_user_id = u.user_id " +
//            "JOIN user_roles AS ur " +
//            "ON u.user_id = ur.user_user_id " +
//            "WHERE n.notification_id = ?";

    private static final String SQL_UPDATE_NOTIFICATION_BY_ID = "UPDATE notifications SET " +
            "message_key = ?, " +
            "topic_key = ?, " +
            "notification_date_time = ?, " +
            "addressed_user_user_id = ? " +
            "WHERE notification_id = ?";

    private static final String SQL_DELETE_MESSAGE_VALUES_BY_NOTIFICATION_ID = "DELETE FROM " +
            "notification_message_values " +
            "WHERE notification_notification_id = ?";

    private static final String SQL_DELETE_TOPIC_VALUES_BY_NOTIFICATION_ID = "DELETE FROM " +
            "notification_topic_values " +
            "WHERE notification_notification_id = ?";

    private static final String SQL_DELETE_NOTIFICATION_BY_ID = "DELETE FROM notifications " +
            "WHERE notification_id = ?";

    private static final String SQL_PATTERN_NOTIFICATION_ID = "n.notification_id = ?";

    private static final String SQL_PATTERN_ADDRESSED_USER_ID = "n.addressed_user_user_id = ?";


    JDBCNotificationDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Notification entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_NOTIFICATION, Statement.RETURN_GENERATED_KEYS)) {
            System.out.println("Saving notification to database:");
            fillPreparedStatement(entity, preparedStatement);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong(1));
            }

            System.out.println("Inserting message values:");
            insertMessageValues(entity);

            System.out.println("Inserting topic values:");
            insertTopicValues(entity);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Notification findById(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_NOTIFICATIONS +
                " WHERE " +
                SQL_PATTERN_NOTIFICATION_ID)) {
            preparedStatement.setLong(1, id);

            return findNotificationsByPreparedStatement(preparedStatement).get(0);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Notification> findAll() {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_NOTIFICATIONS)) {

            return findNotificationsByPreparedStatement(preparedStatement);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Notification> findAllByAddressedUserId(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_NOTIFICATIONS +
                " WHERE " +
                SQL_PATTERN_ADDRESSED_USER_ID)) {
            preparedStatement.setLong(1, id);

            return findNotificationsByPreparedStatement(preparedStatement);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Notification entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_NOTIFICATION_BY_ID)) {

            fillPreparedStatement(entity, preparedStatement);
            preparedStatement.setLong(5, entity.getId());

            connection.setAutoCommit(false);

            preparedStatement.executeUpdate();
            deleteMessageValuesByNotificationId(entity.getId());
            deleteTopicValuesByNotificationId(entity.getId());
            insertMessageValues(entity);
            insertTopicValues(entity);

            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException exception) {
                System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            }
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_NOTIFICATION_BY_ID)) {

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillPreparedStatement(Notification entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, entity.getMessageKey());
        preparedStatement.setString(2, entity.getTopicKey());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getNotificationDateTime()));
        preparedStatement.setLong(4, entity.getAddressedUser().getId());
    }

    private void insertMessageValues(Notification entity) {
        insertStringValues(entity.getMessageValues(), entity.getId(), SQL_INSERT_MESSAGE_VALUES);
    }

    private void insertTopicValues(Notification entity) {
        insertStringValues(entity.getTopicValues(), entity.getId(), SQL_INSERT_TOPIC_VALUES);
    }

    private void insertStringValues(List<String> values, Long id, String preparedStatementString) {
        for (String value : values) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(preparedStatementString)) {
                preparedStatement.setLong(1, id);
                preparedStatement.setString(2, value);
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<Notification> findNotificationsByPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
        Map<Long, Notification> notifications = new HashMap<>();
        Map<Long, User> users = new HashMap<>();

        ResultSet resultSet = preparedStatement.executeQuery();

        NotificationMapper notificationMapper = new NotificationMapper();
        UserMapper userMapper = new UserMapper();

        while (resultSet.next()) {
            Notification notification = notificationMapper.extractFromResultSet(resultSet);
            notification = notificationMapper.makeUnique(notifications, notification);

            User addressedUser = userMapper.extractFromResultSet(resultSet);
            Role role = userMapper.extractRoleFromResultSet(resultSet);
            addressedUser = userMapper.makeUnique(users, addressedUser);
            addressedUser.getRoles().add(role);

            notification.setAddressedUser(addressedUser);
            notification.getMessageValues().add(notificationMapper.extractMessageValueFromResultSet(resultSet));
            notification.getTopicValues().add(notificationMapper.extractTopicValueFromResultSet(resultSet));

        }
        return new ArrayList<>(notifications.values());

    }

    private void deleteMessageValuesByNotificationId(Long id) {
        deleteValuesByNotificationId(id, SQL_DELETE_MESSAGE_VALUES_BY_NOTIFICATION_ID);

    }

    private void deleteTopicValuesByNotificationId(Long id) {
        deleteValuesByNotificationId(id, SQL_DELETE_TOPIC_VALUES_BY_NOTIFICATION_ID);
    }

    private void deleteValuesByNotificationId(Long id, String preparedStatementString) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(preparedStatementString)) {
            preparedStatement.setLong(1, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
