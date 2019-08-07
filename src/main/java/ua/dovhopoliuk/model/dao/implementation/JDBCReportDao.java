package ua.dovhopoliuk.model.dao.implementation;

import ua.dovhopoliuk.model.dao.DaoFactory;
import ua.dovhopoliuk.model.dao.ReportDao;
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
    private final DaoFactory daoFactory = new JDBCDaoFactory();

    private static final String SQL_INSERT_REPORT = "INSERT INTO reports" +
            "(topic, " +
            "conference_id, " +
            "user_id) " +
            "VALUES(?,?,?)";

    private static final String SQL_SELECT_ALL_REPORTS = "SELECT *, " +
            "topic AS 'report_topic' " +
            "FROM reports";

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
        return null;
    }

    @Override
    public List<Report> findAll() {
        return null;
    }

    @Override
    public void update(Report entity) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void close() throws Exception {

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

        while (resultSet.next()) {
            Report report = reportMapper.extractFromResultSet(resultSet);
            report = reportMapper.makeUnique(reports, report);

            Conference conference = daoFactory.createConferenceDao().findById(resultSet.getLong("conference_id"));
            conference = conferenceMapper.makeUnique(conferences, conference);

            User speaker = daoFactory.createUserDao().findById(resultSet.getLong("speaker_id"));
            speaker = userMapper.makeUnique(users, speaker);

            report.setConference(conference);
            report.setSpeaker(speaker);
        }
        return new ArrayList<>(reports.values());
    }
}
