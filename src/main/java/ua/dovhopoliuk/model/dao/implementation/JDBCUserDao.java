package ua.dovhopoliuk.model.dao.implementation;

import ua.dovhopoliuk.model.dao.UserDao;
import ua.dovhopoliuk.model.entity.Role;
import ua.dovhopoliuk.model.entity.User;
import ua.dovhopoliuk.model.exception.LoginNotUniqueException;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JDBCUserDao implements UserDao {
    private final Connection connection;

    private static final String SQL_INSERT = "INSERT INTO users " +
            "(surname, " +
            "name, " +
            "patronymic, " +
            "login, " +
            "email, " +
            "password) " +
            "VALUES (?,?,?,?,?,?)";

    private static final String SQL_INSERT_ROLE = "INSERT INTO user_roles " +
            "(user_id, " +
            "roles) " +
            "VALUES (?,?)";

    private static final String SQL_SELECT_BY_LOGIN = "SELECT * FROM users AS u " +
            "JOIN user_roles AS ur " +
            "ON u.id = ur.user_id " +
            "WHERE u.login = ?";

    JDBCUserDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(User entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            fillPreparedStatement(entity, preparedStatement);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong(1));
            }

            insertRoles(entity);

        } catch (SQLException e) {
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
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public void update(User entity) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public User findByLogin(String login) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_LOGIN)) {
            preparedStatement.setString(1, login);

            ResultSet resultSet = preparedStatement.executeQuery();

            return executeUserFromResultSet(resultSet);

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
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_ROLE)) {
            for (Role role : entity.getRoles()) {
                preparedStatement.setLong(1, entity.getId());
                preparedStatement.setString(2, role.name());

                preparedStatement.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private User executeUserFromResultSet(ResultSet resultSet) throws SQLException {

        if (resultSet.next()) {
            User user = new User();

            user.setId(resultSet.getLong("id"));
            user.setSurname(resultSet.getString("surname"));
            user.setName(resultSet.getString("name"));
            user.setPatronymic(resultSet.getString("patronymic"));
            user.setLogin(resultSet.getString("login"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));

            user.setRoles(executeRolesFromResultSet(resultSet));

            return user;
        } else {
            return null;
        }
    }

    private Set<Role> executeRolesFromResultSet(ResultSet resultSet) throws SQLException {
        Set<Role> result = new HashSet<>();

        resultSet.beforeFirst();
        while (resultSet.next()) {
            result.add(Role.valueOf(resultSet.getString("roles")));
        }

        return result;
    }
}
