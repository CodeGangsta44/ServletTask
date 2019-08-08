package ua.dovhopoliuk.model.dao.implementation;

import ua.dovhopoliuk.model.dao.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCDaoFactory extends DaoFactory {

    @Override
    public UserDao createUserDao() {
        return new JDBCUserDao(getConnection());
    }

    @Override
    public ConferenceDao createConferenceDao() {
        return new JDBCConferenceDao(getConnection());
    }

    @Override
    public ReportDao createReportDao() {
        return new JDBCReportDao(getConnection());
    }

    @Override
    public ReportRequestDao createReportRequestDao() {
        return new JDBCReportRequestDao(getConnection());
    }

    @Override
    public NotificationDao createNotificationDao() {
        return new JDBCNotificationDao(getConnection());
    }

    @Override
    public VoteDao createVoteDao() {
        return new JDBCVoteDao(getConnection());
    }

    private Connection getConnection(){
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/taskdb",
                    "root" ,
                    "12345678" );
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
