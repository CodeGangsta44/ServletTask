package ua.dovhopoliuk.model.dao.implementation;

import ua.dovhopoliuk.model.dao.DaoFactory;
import ua.dovhopoliuk.model.dao.UserDao;
import ua.dovhopoliuk.model.dao.VoteDao;
import ua.dovhopoliuk.model.dao.mapper.UserMapper;
import ua.dovhopoliuk.model.dao.mapper.VoteMapper;
import ua.dovhopoliuk.model.entity.User;
import ua.dovhopoliuk.model.entity.Vote;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCVoteDao implements VoteDao {
    private final Connection connection;
    private final DaoFactory daoFactory = DaoFactory.getInstance();

    private static final String SQL_INSERT_VOTE = "INSERT INTO votes" +
            "(mark, " +
            "speaker_user_id, " +
            "user_user_id) " +
            "VALUES(?,?,?)";

    private static final String SQL_SELECT_ALL_VOTES = "SELECT * FROM votes";

    private static final String SQL_SELECT_VOTE_BY_ID = "SELECT * FROM votes " +
            "WHERE vote_id = ?";

    private static final String SQL_UPDATE_VOTE_BY_ID = "UPDATE votes SET " +
            "mark = ?, " +
            "speaker_user_id = ?, " +
            "user_user_id = ? " +
            "WHERE vote_id = ?";

    private static final String SQL_DELETE_VOTE_BY_ID = "DELETE FROM votes " +
            "WHERE vote_id = ?";

    JDBCVoteDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Vote entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_VOTE, Statement.RETURN_GENERATED_KEYS)) {
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
    public Vote findById(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_VOTE_BY_ID)) {
            preparedStatement.setLong(1, id);

            return findVotesByPreparedStatement(preparedStatement).get(0);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Vote> findAll() {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_VOTES)) {

            return findVotesByPreparedStatement(preparedStatement);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Vote entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_VOTE_BY_ID)) {

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
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_VOTE_BY_ID)) {
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

    private void fillPreparedStatement(Vote entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, entity.getMark());
        preparedStatement.setLong(2, entity.getSpeaker().getId());
        preparedStatement.setLong(3, entity.getUser().getId());
    }

    private List<Vote> findVotesByPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
        Map<Long, Vote> votes = new HashMap<>();
        Map<Long, User> users = new HashMap<>();

        ResultSet resultSet = preparedStatement.executeQuery();

        VoteMapper voteMapper = new VoteMapper();
        UserMapper userMapper = new UserMapper();

        UserDao userDao = daoFactory.createUserDao();

        while (resultSet.next()) {

            Vote vote = voteMapper.extractFromResultSet(resultSet);
            vote = voteMapper.makeUnique(votes, vote);

            User speaker = userDao.findById(resultSet.getLong("speaker_user_id"));
            speaker = userMapper.makeUnique(users, speaker);

            User user = userDao.findById(resultSet.getLong("user_user_id"));
            user = userMapper.makeUnique(users, user);

            vote.setSpeaker(speaker);
            vote.setUser(user);

        }
        return new ArrayList<>(votes.values());
    }
}
