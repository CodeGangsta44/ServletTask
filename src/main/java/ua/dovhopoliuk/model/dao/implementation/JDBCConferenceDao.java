package ua.dovhopoliuk.model.dao.implementation;

import ua.dovhopoliuk.model.dao.ConferenceDao;
import ua.dovhopoliuk.model.dao.DaoFactory;
import ua.dovhopoliuk.model.dao.mapper.ConferenceMapper;
import ua.dovhopoliuk.model.dao.mapper.ReportMapper;
import ua.dovhopoliuk.model.dao.mapper.UserMapper;
import ua.dovhopoliuk.model.entity.Conference;
import ua.dovhopoliuk.model.entity.Report;
import ua.dovhopoliuk.model.entity.User;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class JDBCConferenceDao implements ConferenceDao {
    private final DaoFactory daoFactory = new JDBCDaoFactory();
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

    private static final String SQL_DELETE_REGISTERED_USER = "DELETE * FROM users_conferences " +
            "WHERE user_id = ? AND conference_id = ?";

    private static final String SQL_INSERT_REPORT = "INSERT INTO conferences_reports " +
            "(conference_id, reports_id) " +
            "VALUES(?, ?)";

    private static final String SQL_DELETE_REPORT = "DELETE * FROM conferences_reports " +
            "WHERE conference_id = ? AND reports_id = ?";

    private static final String SQL_SELECT_ALL_CONFERENCES = "SELECT *, " +
            "c.topic AS 'conference_topic', " +
            "r.user_id AS 'speaker_id', " +
            "r.topic AS 'report_topic' " +
            "FROM conferences AS c " +
            "JOIN users_conferences AS uc " +
            "ON c.conference_id = uc.conference_id " +
            "JOIN conferences_reports AS cr " +
            "ON c.conference_id = cr.conference_id " +
            "JOIN reports AS r " +
            "ON cr.reports_id = r.report_id";

    private static final String SQL_SELECT_CONFERENCE_BY_ID = "SELECT *, " +
            "c.topic AS 'conference_topic', " +
            "r.user_id AS 'speaker_id', " +
            "r.topic AS 'report_topic' " +
            "FROM conferences AS c " +
            "JOIN users_conferences AS uc " +
            "ON c.conference_id = uc.conference_id " +
            "JOIN conferences_reports AS cr " +
            "ON c.conference_id = cr.conference_id " +
            "JOIN reports AS r " +
            "ON cr.reports_id = r.report_id " +
            "WHERE c.conference_id = ?";

    private static final String SQL_UPDATE_CONFERENCE_BY_ID = "UPDATE conferences SET " +
            "topic = ?, " +
            "event_date_time = ?, " +
            "event_address = ?, " +
            "description = ?, " +
            "approved = ?, " +
            "finished = ?, " +
            "number_of_visited_guests = ?" +
            "WHERE conference_id = ?";

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
            insertReports(entity.getReports(), entity.getId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Conference findById(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_CONFERENCE_BY_ID)) {
            preparedStatement.setLong(1, id);

            return findConferencesByPreparedStatement(preparedStatement).get(0);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<Conference> findAll() {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_CONFERENCES)) {

            return findConferencesByPreparedStatement(preparedStatement);

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

        Set<Report> reportsToDelete = getReportsToDelete(entity, oldConference);
        Set<Report> reportsToInsert = getReportsToInsert(entity, oldConference);

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_CONFERENCE_BY_ID)) {
            connection.setAutoCommit(false);

            fillPreparedStatement(entity, preparedStatement);
            preparedStatement.setLong(7, entity.getId());
            preparedStatement.executeUpdate();

            deleteRegisteredUsers(usersToDelete, entity.getId());
            insertRegisteredUsers(usersToInsert, entity.getId());

            deleteReports(reportsToDelete, entity.getId());
            insertReports(reportsToInsert, entity.getId());

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

    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    private void fillPreparedStatement(Conference entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, entity.getTopic());
        preparedStatement.setTimestamp(2, Timestamp.valueOf(entity.getEventDateTime()));
        preparedStatement.setString(3, entity.getEventAddress());
        preparedStatement.setString(4, entity.getDescription());
        preparedStatement.setBoolean(5, entity.isApproved());
        preparedStatement.setBoolean(6, entity.isFinished());
        preparedStatement.setLong(7, entity.getNumberOfVisitedGuests());
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

    private void insertReports(Set<Report> reports, Long id) {
        reportsUpdate(reports, id, SQL_INSERT_REPORT);
    }

    private void deleteReports(Set<Report> reports, Long id) {
        reportsUpdate(reports, id, SQL_DELETE_REPORT);
    }

    private void reportsUpdate(Set<Report> reports, Long id, String preparedStatementString) {
        for (Report report : reports) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(preparedStatementString)) {
                preparedStatement.setLong(1, id);
                preparedStatement.setLong(2, report.getId());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Conference> findConferencesByPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
        Map<Long, Conference> conferences = new HashMap<>();
        Map<Long, User> users = new HashMap<>();
        Map<Long, Report> reports = new HashMap<>();

        ResultSet resultSet = preparedStatement.executeQuery();

        UserMapper userMapper = new UserMapper();
        ConferenceMapper conferenceMapper = new ConferenceMapper();
        ReportMapper reportMapper = new ReportMapper();

        while (resultSet.next()) {
            Conference conference = conferenceMapper.extractFromResultSet(resultSet);
            conference = conferenceMapper.makeUnique(conferences, conference);

            User user = daoFactory.createUserDao().findById(resultSet.getLong("user_id"));
            user = userMapper.makeUnique(users, user);

            User speaker = daoFactory.createUserDao().findById(resultSet.getLong("speaker_id"));
            speaker = userMapper.makeUnique(users, speaker);

            Report report = reportMapper.extractFromResultSet(resultSet);
            report = reportMapper.makeUnique(reports, report);

            conference.getRegisteredGuests().add(user);
            conference.getReports().add(report);
            report.setSpeaker(speaker);
            report.setConference(conference);
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

    private Set<Report> getReportsToDelete(Conference newConference, Conference oldConference) {
        return oldConference.getReports()
                .stream()
                .filter(report -> !newConference.getReports().contains(report))
                .collect(Collectors.toSet());
    }

    private Set<Report> getReportsToInsert(Conference newConference, Conference oldConference) {
        return newConference.getReports()
                .stream()
                .filter(report -> !oldConference.getReports().contains(report))
                .collect(Collectors.toSet());
    }
}
