package ua.dovhopoliuk.model.dao;

import ua.dovhopoliuk.model.dao.implementation.JDBCDaoFactory;

public abstract class DaoFactory {
    private static DaoFactory daoFactory;

    public abstract UserDao createUserDao();
    public abstract ConferenceDao createConferenceDao();
    public abstract ReportDao createReportDao();
    public abstract ReportRequestDao createReportRequestDao();
    public abstract NotificationDao createNotificationDao();
    public abstract VoteDao createVoteDao();

    public static DaoFactory getInstance(){
        if (daoFactory == null) {
            synchronized (DaoFactory.class) {
                if (daoFactory == null) {
                    daoFactory = new JDBCDaoFactory();
                }
            }
        }
        return daoFactory;
    }
}
