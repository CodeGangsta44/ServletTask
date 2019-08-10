package ua.dovhopoliuk.model.dao.implementation;

import ua.dovhopoliuk.model.dao.UserDao;
import ua.dovhopoliuk.model.dao.mapper.UserMapper;
import ua.dovhopoliuk.model.entity.Role;
import ua.dovhopoliuk.model.entity.User;
import ua.dovhopoliuk.model.exception.LoginNotUniqueException;

import java.sql.*;
import java.util.*;

public class JDBCUserDao implements UserDao {
    private final Connection connection;

    private static final String SQL_INSERT_USER = "INSERT INTO users " +
            "(surname, " +
            "name, " +
            "patronymic, " +
            "login, " +
            "email, " +
            "password) " +
            "VALUES (?,?,?,?,?,?)";

    private static final String SQL_INSERT_ROLE = "INSERT INTO user_roles " +
            "(user_user_id, " +
            "roles) " +
            "VALUES (?,?)";


    private static final String SQL_SELECT_BY_LOGIN = "SELECT * FROM users AS u " +
            "JOIN user_roles AS ur " +
            "ON u.user_id = ur.user_user_id " +
            "WHERE u.login = ?";

    private static final String SQL_SELECT_BY_ID = "SELECT * FROM users AS u " +
            "JOIN user_roles AS ur " +
            "ON u.user_id = ur.user_user_id " +
            "WHERE u.user_id = ?";

    private static final String SQL_SELECT_ALL_USERS = "SELECT * FROM users AS u " +
            "JOIN user_roles AS ur " +
            "ON u.user_id = ur.user_user_id";

    private static final String SQL_DELETE_ROLES_BY_USER_ID = "DELETE * FROM user_roles " +
            "WHERE user_user_id = ?";

    private static final String SQL_UPDATE_USER_BY_ID = "UPDATE users SET " +
            "surname = ?, " +
            "name = ?, " +
            "patronymic = ?, " +
            "login = ?, " +
            "email = ?," +
            "password = ? " +
            "WHERE user_id = ?";

    private static final String SQL_DELETE_USER_BY_ID = "DELETE FROM users " +
            "WHERE user_id = ?";

    JDBCUserDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(User entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
            fillPreparedStatement(entity, preparedStatement);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong(1));
            }

            insertRoles(entity);

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            if (e.getErrorCode() == 1062) {
                e.printStackTrace();
                throw new LoginNotUniqueException("Entered login is not unique: ", entity.getLogin());
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findById(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);

            return findUsersByPreparedStatement(preparedStatement).get(0);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findAll() {

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_USERS)) {

            return findUsersByPreparedStatement(preparedStatement);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(User entity) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_USER_BY_ID)) {

            fillPreparedStatement(entity, preparedStatement);
            preparedStatement.setLong(7, entity.getId());

            connection.setAutoCommit(false);

            preparedStatement.executeUpdate();
            deleteRoles(entity.getId());
            insertRoles(entity);

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
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_USER_BY_ID)) {
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

    @Override
    public User findByLogin(String login) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_LOGIN)) {
            preparedStatement.setString(1, login);

            return findUsersByPreparedStatement(preparedStatement).get(0);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void fillPreparedStatement(User entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, entity.getSurname());
        preparedStatement.setString(2, entity.getName());
        preparedStatement.setString(3, entity.getPatronymic());
        preparedStatement.setString(4, entity.getLogin());
        preparedStatement.setString(5, entity.getEmail());
        preparedStatement.setString(6, entity.getPassword());
    }

    private void insertRoles(User entity) {

        for (Role role : entity.getRoles()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_ROLE)) {
                preparedStatement.setLong(1, entity.getId());
                preparedStatement.setString(2, role.name());
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteRoles(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_ROLES_BY_USER_ID)) {
            preparedStatement.setLong(1, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<User> findUsersByPreparedStatement( PreparedStatement preparedStatement) throws SQLException {
        Map<Long, User> users = new HashMap<>();

        ResultSet resultSet = preparedStatement.executeQuery();

        UserMapper userMapper = new UserMapper();

        while (resultSet.next()) {
            User user = userMapper.extractFromResultSet(resultSet);
            Role role = userMapper.extractRoleFromResultSet(resultSet);
            user = userMapper.makeUnique(users, user);
            user.getRoles().add(role);
        }
        return new ArrayList<>(users.values());

    }

}
