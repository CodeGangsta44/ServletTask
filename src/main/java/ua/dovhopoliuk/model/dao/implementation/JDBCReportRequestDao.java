package ua.dovhopoliuk.model.dao.implementation;

import ua.dovhopoliuk.model.dao.ConferenceDao;
import ua.dovhopoliuk.model.dao.DaoFactory;
import ua.dovhopoliuk.model.dao.ReportRequestDao;
import ua.dovhopoliuk.model.dao.UserDao;
import ua.dovhopoliuk.model.dao.mapper.ConferenceMapper;
import ua.dovhopoliuk.model.dao.mapper.ReportRequestMapper;
import ua.dovhopoliuk.model.dao.mapper.UserMapper;
import ua.dovhopoliuk.model.entity.Conference;
import ua.dovhopoliuk.model.entity.ReportRequest;
import ua.dovhopoliuk.model.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCReportRequestDao implements ReportRequestDao {
    private final Connection connection;
    private final DaoFactory daoFactory = new JDBCDaoFactory();

    private static final String SQL_INSERT_REPORT_REQUEST = "INSERT INTO report_requests" +
            "(topic, " +
            "conference_conference_id, " +
            "user_id, " +
            "approved_by_moderator, " +
            "approved_by_speaker) " +
            "VALUES(?,?,?,?,?)";

    private static final String SQL_SELECT_ALL_REPORT_REQUESTS = "SELECT * FROM report_requests";

    private static final String SQL_SELECT_REPORT_REQUEST_BY_ID = "SELECT * FROM report_requests " +
            "WHERE report_request_id = ?";

    private static final String SQL_UPDATE_REPORT_REQUEST_BY_ID = "UPDATE report_requests SET " +
            "topic = ?, " +
            "conference_conference_id = ?, " +
            "user_id = ?, " +
            "approved_by_moderator = ?, " +
            "approved_by_speaker = ? " +
            "WHERE report_request_id = ?";

    private static final String SQL_DELETE_REPORT_REQUEST_BY_ID = "DELETE FROM report_requests " +
            "WHERE report_request_id = ?";

    JDBCReportRequestDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(ReportRequest entity) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(SQL_INSERT_REPORT_REQUEST, Statement.RETURN_GENERATED_KEYS)) {

            fillPreparedStatement(entity, preparedStatement);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong(1));
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ReportRequest findById(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_REPORT_REQUEST_BY_ID)) {
            preparedStatement.setLong(1, id);

            return findReportRequestsByPreparedStatement(preparedStatement).get(0);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ReportRequest> findAll() {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_REPORT_REQUESTS)) {

            return findReportRequestsByPreparedStatement(preparedStatement);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(ReportRequest entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_REPORT_REQUEST_BY_ID)) {

            fillPreparedStatement(entity, preparedStatement);
            preparedStatement.setLong(6, entity.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_REPORT_REQUEST_BY_ID)) {
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    private void fillPreparedStatement(ReportRequest entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, entity.getTopic());
        preparedStatement.setLong(2, entity.getConference().getId());
        preparedStatement.setLong(3, entity.getSpeaker().getId());
        preparedStatement.setBoolean(4, entity.isApprovedByModerator());
        preparedStatement.setBoolean(5, entity.isApprovedBySpeaker());
    }

    private List<ReportRequest> findReportRequestsByPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
        Map<Long, ReportRequest> reportRequests = new HashMap<>();
        Map<Long, User> users = new HashMap<>();
        Map<Long, Conference> conferences = new HashMap<>();

        ResultSet resultSet = preparedStatement.executeQuery();

        ReportRequestMapper reportRequestMapper = new ReportRequestMapper();
        UserMapper userMapper = new UserMapper();
        ConferenceMapper conferenceMapper = new ConferenceMapper();

        UserDao userDao = daoFactory.createUserDao();
        ConferenceDao conferenceDao = daoFactory.createConferenceDao();

        while (resultSet.next()) {
            ReportRequest reportRequest = reportRequestMapper.extractFromResultSet(resultSet);
            reportRequest = reportRequestMapper.makeUnique(reportRequests, reportRequest);

            Conference conference = conferenceDao.findById(resultSet.getLong("conference_conference_id"));
            conference = conferenceMapper.makeUnique(conferences, conference);

            User speaker = userDao.findById(resultSet.getLong("user_id"));
            speaker = userMapper.makeUnique(users, speaker);

            reportRequest.setConference(conference);
            reportRequest.setSpeaker(speaker);
        }
        return new ArrayList<>(reportRequests.values());

    }
}
