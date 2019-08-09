package ua.dovhopoliuk.model.service;

import ua.dovhopoliuk.model.dao.DaoFactory;
import ua.dovhopoliuk.model.entity.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

        daoFactory.createReportRequestDao().create(reportRequest);

        approve(request, reportRequest);
    }

    public List<ReportRequest> getAllReportRequests() {
        return daoFactory.createReportRequestDao().findAll();
    }

    public List<ReportRequest> getProposedReports (HttpServletRequest request) {
        User speaker = userService.getCurrentUser(request);

        return daoFactory.createReportRequestDao().findAll().stream()
                .filter(ReportRequest::isApprovedByModerator)
                .filter(reportRequest -> speaker.equals(reportRequest.getSpeaker()))
                .collect(Collectors.toList());

        // TODO: implement this method
//        return reportRequestRepository.findAllByApprovedByModeratorIsTrue().stream()
//                .filter(request -> speaker.equals(request.getSpeaker()))
//                .collect(Collectors.toList());
    }

    public void approve(HttpServletRequest request, Long reportRequestId) {
        ReportRequest reportRequest = daoFactory.createReportRequestDao().findById(reportRequestId);
        approve(request, reportRequest);
    }

    public void reject(HttpServletRequest request, Long reportRequestId) {
        ReportRequest reportRequest = daoFactory.createReportRequestDao().findById(reportRequestId);
        reject(request, reportRequest);
    }

    public void approve(HttpServletRequest request, ReportRequest reportRequest) {
        User currentUser = userService.getCurrentUser(request);

        if (currentUser.getRoles().contains(Role.MODER)) {
            reportRequest.setApprovedByModerator(true);
        }

        if (currentUser.equals(reportRequest.getSpeaker())) {
            reportRequest.setApprovedBySpeaker(true);
        }

        daoFactory.createReportRequestDao().update(reportRequest);

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

        daoFactory.createNotificationDao().create(notification);
        daoFactory.createReportDao().create(report);
        daoFactory.createReportRequestDao().delete(reportRequest.getId());
    }

    public void reject(HttpServletRequest request, ReportRequest reportRequest) {
        Conference conference = reportRequest.getConference();
        User speaker = reportRequest.getSpeaker();

        User currentUser = userService.getCurrentUser(request);

        Notification notification = createNotification(reportRequest, speaker, conference, "rejected");

        daoFactory.createNotificationDao().create(notification);
        daoFactory.createReportRequestDao().delete(reportRequest.getId());
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
