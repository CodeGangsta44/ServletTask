package ua.dovhopoliuk.model.dao.implementation;

import ua.dovhopoliuk.model.dao.ConferenceDao;
import ua.dovhopoliuk.model.dao.mapper.ConferenceMapper;
import ua.dovhopoliuk.model.dao.mapper.ReportMapper;
import ua.dovhopoliuk.model.dao.mapper.UserMapper;
import ua.dovhopoliuk.model.entity.Conference;
import ua.dovhopoliuk.model.entity.Report;
import ua.dovhopoliuk.model.entity.Role;
import ua.dovhopoliuk.model.entity.User;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class JDBCConferenceDao implements ConferenceDao {
    private final Connection connection;

    private static final String SQL_INSERT_CONFERENCE = "INSERT INTO conferences" +
            "(topic, " +
            "event_date_time, " +
            "event_address, " +
            "description, " +
            "approved, " +
            "finished, " +
            "number_of_visited_guests) " +
            "VALUES(?,?,?,?,?,?,?)";

    private static final String SQL_INSERT_REGISTERED_USER = "INSERT INTO users_conferences " +
            "(conference_id, user_id) " +
            "VALUES(?, ?)";

    private static final String SQL_DELETE_REGISTERED_USER = "DELETE FROM users_conferences " +
            "WHERE conference_id = ? AND user_id = ?";


    private static final String SQL_SELECT_ALL_CONFERENCES_WITH_REGISTERED_USERS = "SELECT *, " +
            "c.topic AS 'conference_topic', " +
            "r.user_id AS 'speaker_id', " +
            "r.topic AS 'report_topic' " +
            "FROM conferences AS c " +
            "LEFT JOIN users_conferences AS uc " +
            "ON c.conference_id = uc.conference_id " +
            "LEFT JOIN reports AS r " +
            "ON c.conference_id = r.conference_id " +
            "LEFT JOIN users AS u " +
            "ON uc.user_id = u.user_id " +
            "LEFT JOIN user_roles AS ur " +
            "ON u.user_id = ur.user_user_id";

    private static final String SQL_SELECT_ALL_CONFERENCES_WITH_REPORTS = "SELECT *, " +
            "c.topic AS 'conference_topic', " +
            "r.user_id AS 'speaker_id', " +
            "r.topic AS 'report_topic' " +
            "FROM conferences AS c " +
            "LEFT JOIN users_conferences AS uc " +
            "ON c.conference_id = uc.conference_id " +
            "LEFT JOIN reports AS r " +
            "ON c.conference_id = r.conference_id " +
            "LEFT JOIN users AS u " +
            "ON r.user_id = u.user_id " +
            "LEFT JOIN user_roles AS ur " +
            "ON u.user_id = ur.user_user_id";


    private static final String SQL_UPDATE_CONFERENCE_BY_ID = "UPDATE conferences SET " +
            "topic = ?, " +
            "event_date_time = ?, " +
            "event_address = ?, " +
            "description = ?, " +
            "approved = ?, " +
            "finished = ?, " +
            "number_of_visited_guests = ? " +
            "WHERE conference_id = ?";

    private static final String SQL_DELETE_CONFERENCE_BY_ID = "DELETE FROM conferences " +
            "WHERE conference_id = ?";

    private static final String SQL_PATTERN_CONFERENCE_ID = "c.conference_id = ?";

    private static final String SQL_SUB_SELECT_CONFERENCE_ID_BY_USER_ID = "(SELECT conference_id " +
            "FROM users_conferences " +
            "WHERE user_id = ?)";

    private static final String SQL_PATTERN_CONFERENCE_FINISHED = "c.finished = ?";

    private static final String SQL_PATTERN_CONFERENCE_APPROVED = "c.approved = ?";


    JDBCConferenceDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Conference entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_CONFERENCE, Statement.RETURN_GENERATED_KEYS)) {
            fillPreparedStatement(entity, preparedStatement);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong(1));
            }

            insertRegisteredUsers(entity.getRegisteredGuests(), entity.getId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Conference findById(Long id) {
        try (PreparedStatement preparedStatementForUsers = connection
                .prepareStatement(SQL_SELECT_ALL_CONFERENCES_WITH_REGISTERED_USERS + " WHERE " + SQL_PATTERN_CONFERENCE_ID);

             PreparedStatement preparedStatementForReports = connection
                     .prepareStatement(SQL_SELECT_ALL_CONFERENCES_WITH_REPORTS + " WHERE " +  SQL_PATTERN_CONFERENCE_ID)) {
            preparedStatementForUsers.setLong(1, id);
            preparedStatementForReports.setLong(1, id);

            return findConferencesByPreparedStatements(preparedStatementForUsers, preparedStatementForReports).get(0);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<Conference> findAll() {
        try (PreparedStatement preparedStatementForUsers = connection.prepareStatement(SQL_SELECT_ALL_CONFERENCES_WITH_REGISTERED_USERS);
             PreparedStatement preparedStatementForReports = connection.prepareStatement(SQL_SELECT_ALL_CONFERENCES_WITH_REPORTS)) {

            return findConferencesByPreparedStatements(preparedStatementForUsers, preparedStatementForReports);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Conference> findAllByApprovedIsTrueAndFinishedIsFalse() {
        try (PreparedStatement preparedStatementForUsers = connection
                .prepareStatement(SQL_SELECT_ALL_CONFERENCES_WITH_REGISTERED_USERS +
                        " WHERE " +
                        SQL_PATTERN_CONFERENCE_APPROVED +
                        " AND " +
                        SQL_PATTERN_CONFERENCE_FINISHED);

             PreparedStatement preparedStatementForReports = connection
                     .prepareStatement(SQL_SELECT_ALL_CONFERENCES_WITH_REPORTS +
                             " WHERE " +
                             SQL_PATTERN_CONFERENCE_APPROVED +
                             " AND " +
                             SQL_PATTERN_CONFERENCE_FINISHED)) {

            preparedStatementForUsers.setBoolean(1, true);
            preparedStatementForUsers.setBoolean(2, false);
            preparedStatementForReports.setBoolean(1, true);
            preparedStatementForReports.setBoolean(2, false);

            return findConferencesByPreparedStatements(preparedStatementForUsers, preparedStatementForReports);


        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Conference> findAllByApprovedIsFalse() {
        try (PreparedStatement preparedStatementForUsers = connection
                .prepareStatement(SQL_SELECT_ALL_CONFERENCES_WITH_REGISTERED_USERS +
                        " WHERE " +
                        SQL_PATTERN_CONFERENCE_APPROVED);

             PreparedStatement preparedStatementForReports = connection
                     .prepareStatement(SQL_SELECT_ALL_CONFERENCES_WITH_REPORTS +
                             " WHERE " +
                             SQL_PATTERN_CONFERENCE_APPROVED)) {

            System.out.println(SQL_SELECT_ALL_CONFERENCES_WITH_REPORTS +
                    " WHERE " +
                    SQL_PATTERN_CONFERENCE_APPROVED);

            preparedStatementForUsers.setBoolean(1, false);
            preparedStatementForReports.setBoolean(1, false);

            return findConferencesByPreparedStatements(preparedStatementForUsers, preparedStatementForReports);


        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Conference> findAllByFinishedIsTrue() {
        try (PreparedStatement preparedStatementForUsers = connection
                .prepareStatement(SQL_SELECT_ALL_CONFERENCES_WITH_REGISTERED_USERS +
                        " WHERE " +
                        SQL_PATTERN_CONFERENCE_FINISHED);

             PreparedStatement preparedStatementForReports = connection
                     .prepareStatement(SQL_SELECT_ALL_CONFERENCES_WITH_REPORTS +
                             " WHERE " +
                             SQL_PATTERN_CONFERENCE_FINISHED)) {

            preparedStatementForUsers.setBoolean(1, true);
            preparedStatementForReports.setBoolean(1, true);

            return findConferencesByPreparedStatements(preparedStatementForUsers, preparedStatementForReports);


        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Conference> findAllByRegisteredGuestsContainsAndApprovedIsTrueAndFinishedIsFalse(Long userId) {
        try (PreparedStatement preparedStatementForUsers = connection
                .prepareStatement(SQL_SELECT_ALL_CONFERENCES_WITH_REGISTERED_USERS +
                        " WHERE " +
                        SQL_PATTERN_CONFERENCE_APPROVED +
                        " AND " +
                        SQL_PATTERN_CONFERENCE_FINISHED +
                        " AND c.conference_id IN" + SQL_SUB_SELECT_CONFERENCE_ID_BY_USER_ID);

             PreparedStatement preparedStatementForReports = connection
                     .prepareStatement(SQL_SELECT_ALL_CONFERENCES_WITH_REPORTS +
                             " WHERE " +
                             SQL_PATTERN_CONFERENCE_APPROVED +
                             " AND " +
                             SQL_PATTERN_CONFERENCE_FINISHED +
                             " AND c.conference_id IN" + SQL_SUB_SELECT_CONFERENCE_ID_BY_USER_ID)) {

            preparedStatementForUsers.setBoolean(1, true);
            preparedStatementForUsers.setBoolean(2, false);
            preparedStatementForUsers.setLong(3, userId);
            preparedStatementForReports.setBoolean(1, true);
            preparedStatementForReports.setBoolean(2, false);
            preparedStatementForReports.setLong(3, userId);

            return findConferencesByPreparedStatements(preparedStatementForUsers, preparedStatementForReports);


        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Conference entity) {
        Conference oldConference = findById(entity.getId());

        Set<User> usersToDelete = getUsersToDelete(entity, oldConference);
        Set<User> usersToInsert = getUsersToInsert(entity, oldConference);


        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_CONFERENCE_BY_ID)) {

            fillPreparedStatement(entity, preparedStatement);
            preparedStatement.setLong(8, entity.getId());

            connection.setAutoCommit(false);

            preparedStatement.executeUpdate();
            deleteRegisteredUsers(usersToDelete, entity.getId());
            insertRegisteredUsers(usersToInsert, entity.getId());

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
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delete(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_CONFERENCE_BY_ID)) {
            connection.setAutoCommit(false);
            preparedStatement.setLong(1, id);

            deleteRegisteredUsers(findById(id).getRegisteredGuests(), id);
            preparedStatement.executeUpdate();

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
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillPreparedStatement(Conference entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, entity.getTopic());
        preparedStatement.setTimestamp(2, Timestamp.valueOf(entity.getEventDateTime()));
        preparedStatement.setString(3, entity.getEventAddress());
        preparedStatement.setString(4, entity.getDescription());
        preparedStatement.setBoolean(5, entity.isApproved());
        preparedStatement.setBoolean(6, entity.isFinished());

        if (!Objects.isNull(entity.getNumberOfVisitedGuests())) {
            preparedStatement.setLong(7, entity.getNumberOfVisitedGuests());
        } else {
            preparedStatement.setNull(7, Types.BIGINT);
        }
    }

    private void insertRegisteredUsers(Set<User> users, Long id) {
        registeredUsersUpdate(users, id, SQL_INSERT_REGISTERED_USER);
    }

    private void deleteRegisteredUsers(Set<User> users, Long id) {
        registeredUsersUpdate(users, id, SQL_DELETE_REGISTERED_USER);
    }

    private void registeredUsersUpdate(Set<User> users, Long id, String preparedStatementString) {
        for (User user : users) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(preparedStatementString)) {
                preparedStatement.setLong(1, id);
                preparedStatement.setLong(2, user.getId());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Conference> findConferencesByPreparedStatements(PreparedStatement preparedStatementForUsers,
                                                                 PreparedStatement preparedStatementForReports) throws SQLException {
        Map<Long, Conference> conferences = new HashMap<>();
        Map<Long, User> users = new HashMap<>();
        Map<Long, Report> reports = new HashMap<>();

        UserMapper userMapper = new UserMapper();
        ConferenceMapper conferenceMapper = new ConferenceMapper();
        ReportMapper reportMapper = new ReportMapper();


        ResultSet resultSetWithUsers = preparedStatementForUsers.executeQuery();
        while (resultSetWithUsers.next()) {
            Conference conference = conferenceMapper.extractFromResultSet(resultSetWithUsers);
            conference = conferenceMapper.makeUnique(conferences, conference);

            if (resultSetWithUsers.getLong("user_id") != 0) {
                User user = userMapper.extractFromResultSet(resultSetWithUsers);
                Role role = userMapper.extractRoleFromResultSet(resultSetWithUsers);
                user = userMapper.makeUnique(users, user);
                user.getRoles().add(role);
                conference.getRegisteredGuests().add(user);
            }
        }

        ResultSet resultSetWithReports = preparedStatementForReports.executeQuery();
        while (resultSetWithReports.next()) {
            Conference conference = conferenceMapper.extractFromResultSet(resultSetWithReports);
            conference = conferenceMapper.makeUnique(conferences, conference);

            if (resultSetWithReports.getLong("report_id") != 0) {
                User speaker = userMapper.extractFromResultSet(resultSetWithReports);
                Role role = userMapper.extractRoleFromResultSet(resultSetWithReports);
                speaker = userMapper.makeUnique(users, speaker);
                speaker.getRoles().add(role);

                Report report = reportMapper.extractFromResultSet(resultSetWithReports);
                report = reportMapper.makeUnique(reports, report);

                report.setSpeaker(speaker);
                report.setConference(conference);
                conference.getReports().add(report);
            }
        }

        return new ArrayList<>(conferences.values());
    }

    private Set<User> getUsersToDelete(Conference newConference, Conference oldConference) {
        return oldConference.getRegisteredGuests()
                .stream()
                .filter(user -> !newConference.getRegisteredGuests().contains(user))
                .collect(Collectors.toSet());
    }

    private Set<User> getUsersToInsert(Conference newConference, Conference oldConference) {
        return newConference.getRegisteredGuests()
                .stream()
                .filter(user -> !oldConference.getRegisteredGuests().contains(user))
                .collect(Collectors.toSet());
    }

}
