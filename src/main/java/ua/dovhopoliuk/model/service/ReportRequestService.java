package ua.dovhopoliuk.model.service;

import ua.dovhopoliuk.model.dao.DaoFactory;
import ua.dovhopoliuk.model.dao.NotificationDao;
import ua.dovhopoliuk.model.dao.ReportDao;
import ua.dovhopoliuk.model.dao.ReportRequestDao;
import ua.dovhopoliuk.model.dao.implementation.TransactionalJDBCDaoFactory;
import ua.dovhopoliuk.model.entity.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReportRequestService {
    private final DaoFactory daoFactory = DaoFactory.getInstance();
    private UserService userService;

    public ReportRequestService(UserService userService) {
        this.userService = userService;
    }

    public void createReportRequest(HttpServletRequest request, ReportRequest reportRequest, Long conferenceId) {

        reportRequest.setConference(daoFactory
                .createConferenceDao()
                .findById(conferenceId));

        if (Objects.isNull(reportRequest.getSpeaker())) {
            reportRequest.setSpeaker(userService.getCurrentUser(request));
        }

        try (ReportRequestDao reportRequestDao = daoFactory.createReportRequestDao()) {
            reportRequestDao.create(reportRequest);
        }

        approve(request, reportRequest);
    }

    public List<ReportRequest> getAllReportRequests() {
        try (ReportRequestDao reportRequestDao = daoFactory.createReportRequestDao()) {
            return reportRequestDao.findAll();
        }
    }

    public ReportRequest getReportRequestById(Long reportRequestId) {
        try (ReportRequestDao reportRequestDao = daoFactory.createReportRequestDao()) {
            return reportRequestDao.findById(reportRequestId);
        }
    }

    public List<ReportRequest> getProposedReports (HttpServletRequest request) {
        User speaker = userService.getCurrentUser(request);

        try (ReportRequestDao reportRequestDao = daoFactory.createReportRequestDao()) {
            return reportRequestDao.findAllByApprovedByModeratorIsTrueAndSpeakerId(speaker.getId());
        }
    }

    public void approve(HttpServletRequest request, Long reportRequestId) {
        ReportRequest reportRequest = getReportRequestById(reportRequestId);
        approve(request, reportRequest);
    }

    public void reject(HttpServletRequest request, Long reportRequestId) {
        ReportRequest reportRequest = getReportRequestById(reportRequestId);
        reject(request, reportRequest);
    }

    private void approve(HttpServletRequest request, ReportRequest reportRequest) {
        User currentUser = userService.getCurrentUser(request);


        if (currentUser.getRoles().contains(Role.MODER)) {
            reportRequest.setApprovedByModerator(true);
        }

        if (currentUser.equals(reportRequest.getSpeaker())) {
            reportRequest.setApprovedBySpeaker(true);
        }

        try (ReportRequestDao reportRequestDao = daoFactory.createReportRequestDao()) {
            reportRequestDao.update(reportRequest);
        }


        if (reportRequest.isApprovedByModerator() && reportRequest.isApprovedBySpeaker()) {
            approveRequest(reportRequest);
        }
    }

    private void approveRequest(ReportRequest reportRequest) {
        Conference conference = reportRequest.getConference();
        Report report = new Report();

        report.setTopic(reportRequest.getTopic());
        report.setConference(reportRequest.getConference());
        report.setSpeaker(reportRequest.getSpeaker());

        User speaker = report.getSpeaker();

        conference.getReports().add(report);

        Notification notification = createNotification(reportRequest, speaker, conference, "approved");

        TransactionalJDBCDaoFactory transactionalDaoFactory = new TransactionalJDBCDaoFactory();
        NotificationDao notificationDao = transactionalDaoFactory.createNotificationDao();
        ReportDao reportDao = transactionalDaoFactory.createReportDao();
        ReportRequestDao reportRequestDao = transactionalDaoFactory.createReportRequestDao();

        try {
            transactionalDaoFactory.beginTransaction();

            notificationDao.create(notification);
            reportDao.create(report);
            reportRequestDao.delete(reportRequest.getId());

            transactionalDaoFactory.commitTransaction();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                transactionalDaoFactory.rollbackTransaction();
            } catch (SQLException er) {
                er.printStackTrace();
            }
        } finally {
            transactionalDaoFactory.close();
        }
    }

    private void reject(HttpServletRequest request, ReportRequest reportRequest) {
        Conference conference = reportRequest.getConference();
        User speaker = reportRequest.getSpeaker();

        User currentUser = userService.getCurrentUser(request);

        Notification notification = createNotification(reportRequest, speaker, conference, "rejected");

        TransactionalJDBCDaoFactory transactionalDaoFactory = new TransactionalJDBCDaoFactory();
        NotificationDao notificationDao = transactionalDaoFactory.createNotificationDao();
        ReportRequestDao reportRequestDao = transactionalDaoFactory.createReportRequestDao();

        try {
            transactionalDaoFactory.beginTransaction();

            notificationDao.create(notification);
            reportRequestDao.delete(reportRequest.getId());

            transactionalDaoFactory.commitTransaction();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                transactionalDaoFactory.rollbackTransaction();
            } catch (SQLException er) {
                er.printStackTrace();
            }
        } finally {
            transactionalDaoFactory.close();
        }
    }

    private Notification createNotification(ReportRequest reportRequest, User speaker, Conference conference, String status) {
        List<String> topic_values = new ArrayList<>();
        List<String> message_values = new ArrayList<>();

        topic_values.add(reportRequest.getTopic());
        message_values.add(speaker.getName());
        message_values.add(reportRequest.getTopic());
        message_values.add(conference.getTopic());

        Notification notification = new Notification();

        notification.setAddressedUser(speaker);
        notification.setNotificationDateTime(LocalDateTime.now());
        notification.setTopicKey("topic.report.request." + status);
        notification.setTopicValues(topic_values);
        notification.setMessageKey("message.report.request." + status);
        notification.setMessageValues(message_values);

        return notification;
    }
}
