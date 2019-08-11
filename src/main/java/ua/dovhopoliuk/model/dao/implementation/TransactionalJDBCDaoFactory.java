package ua.dovhopoliuk.model.dao.implementation;

import ua.dovhopoliuk.model.dao.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class TransactionalJDBCDaoFactory extends JDBCDaoFactory {
    private Connection connection;


    private Connection getConnection(){
        if (Objects.isNull(connection)) {
            try {
                connection =  ConnectionPoolHolder.getDataSource().getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    public void beginTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }

    public void commitTransaction() throws SQLException {
        connection.commit();
        connection.setAutoCommit(true);
    }

    public void rollbackTransaction() throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
