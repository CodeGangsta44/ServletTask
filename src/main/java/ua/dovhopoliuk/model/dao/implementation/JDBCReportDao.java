package ua.dovhopoliuk.model.dao.implementation;

import ua.dovhopoliuk.model.dao.ConferenceDao;
import ua.dovhopoliuk.model.dao.DaoFactory;
import ua.dovhopoliuk.model.dao.ReportDao;
import ua.dovhopoliuk.model.dao.UserDao;
import ua.dovhopoliuk.model.dao.mapper.ConferenceMapper;
import ua.dovhopoliuk.model.dao.mapper.ReportMapper;
import ua.dovhopoliuk.model.dao.mapper.UserMapper;
import ua.dovhopoliuk.model.entity.Conference;
import ua.dovhopoliuk.model.entity.Report;
import ua.dovhopoliuk.model.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCReportDao implements ReportDao {
    private final Connection connection;
    private final DaoFactory daoFactory = DaoFactory.getInstance();

    private static final String SQL_INSERT_REPORT = "INSERT INTO reports" +
            "(topic, " +
            "conference_id, " +
            "user_id) " +
            "VALUES(?,?,?)";

    private static final String SQL_SELECT_ALL_REPORTS = "SELECT *, " +
            "topic AS 'report_topic' " +
            "FROM reports";

    private static final String SQL_SELECT_REPORT_BY_ID = "SELECT *, " +
            "topic AS 'report_topic' " +
            "FROM reports " +
            "WHERE report_id = ?";

    private static final String SQL_SELECT_ALL_REPORTS_BY_SPEAKER_ID = "SELECT *, " +
            "topic AS 'report_topic' " +
            "FROM reports " +
            "WHERE user_id = ?";

    private static final String SQL_SELECT_ALL_COMPLETED_REPORTS_BY_SPEAKER_ID = "SELECT *, " +
            "r.topic AS 'report_topic' " +
            "FROM reports AS r " +
            "JOIN conferences AS c " +
            "ON r.conference_id = c.conference_id " +
            "WHERE c.finished = true " +
            "AND user_id = ?";

    private static final String SQL_UPDATE_REPORT_BY_ID = "UPDATE reports SET " +
            "topic = ?, " +
            "conference_id = ?, " +
            "user_id = ? " +
            "WHERE report_id = ?";

    private static final String SQL_DELETE_REPORT_BY_ID = "DELETE FROM reports " +
            "WHERE report_id = ?";


    JDBCReportDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Report entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_REPORT, Statement.RETURN_GENERATED_KEYS)) {
            fillPreparedStatement(entity, preparedStatement);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Report findById(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_REPORT_BY_ID)) {
            preparedStatement.setLong(1, id);

            return findReportsByPreparedStatement(preparedStatement).get(0);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Report> findAll() {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_REPORTS)) {

            return findReportsByPreparedStatement(preparedStatement);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Report> findAllBySpeakerId(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_REPORTS_BY_SPEAKER_ID)) {
            preparedStatement.setLong(1, id);

            return findReportsByPreparedStatement(preparedStatement);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Report> findAllBySpeakerIdAndConferenceIsFinished(Long id) {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(SQL_SELECT_ALL_COMPLETED_REPORTS_BY_SPEAKER_ID)) {
            preparedStatement.setLong(1, id);

            return findReportsByPreparedStatement(preparedStatement);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Report entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_REPORT_BY_ID)) {
            fillPreparedStatement(entity, preparedStatement);
            preparedStatement.setLong(4, entity.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_REPORT_BY_ID)) {
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

    private void fillPreparedStatement(Report entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, entity.getTopic());
        preparedStatement.setLong(2, entity.getConference().getId());
        preparedStatement.setLong(3, entity.getSpeaker().getId());
    }

    private List<Report> findReportsByPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
        Map<Long, Conference> conferences = new HashMap<>();
        Map<Long, User> users = new HashMap<>();
        Map<Long, Report> reports = new HashMap<>();

        ResultSet resultSet = preparedStatement.executeQuery();

        UserMapper userMapper = new UserMapper();
        ConferenceMapper conferenceMapper = new ConferenceMapper();
        ReportMapper reportMapper = new ReportMapper();

        ConferenceDao conferenceDao = daoFactory.createConferenceDao();
        UserDao userDao = daoFactory.createUserDao();

        while (resultSet.next()) {
            Report report = reportMapper.extractFromResultSet(resultSet);
            report = reportMapper.makeUnique(reports, report);

            Conference conference = conferenceDao.findById(resultSet.getLong("conference_id"));
            conference = conferenceMapper.makeUnique(conferences, conference);

            User speaker = userDao.findById(resultSet.getLong("speaker_id"));
            speaker = userMapper.makeUnique(users, speaker);

            report.setConference(conference);
            report.setSpeaker(speaker);
        }

        conferenceDao.close();
        userDao.close();

        return new ArrayList<>(reports.values());
    }
}
