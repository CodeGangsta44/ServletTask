package ua.dovhopoliuk.model.service;

import ua.dovhopoliuk.model.dao.DaoFactory;
import ua.dovhopoliuk.model.entity.Conference;
import ua.dovhopoliuk.model.entity.Report;
import ua.dovhopoliuk.model.entity.User;
import ua.dovhopoliuk.model.exception.ConferenceNotValidException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConferenceService {
    private final DaoFactory daoFactory = DaoFactory.getInstance();
    private UserService userService;
    private ReportService reportService;
    private ReportRequestService reportRequestService;

    ConferenceService(UserService userService,
                      ReportService reportService,
                      ReportRequestService reportRequestService) {

        this.userService = userService;
        this.reportService = reportService;
        this.reportRequestService = reportRequestService;
    }

    public List<Conference> getAllValidConferences() {
        return this.daoFactory.createConferenceDao().findAll().stream()
                .filter(Conference::isApproved)
                .filter(conference -> !conference.isFinished())
                .collect(Collectors.toList());
        // TODO: implement this method
//        return this.conferenceRepository.findAllByApprovedIsTrueAndFinishedIsFalse();
    }

    public List<Conference> getAllNotApprovedConferences() {
        return this.daoFactory.createConferenceDao().findAll().stream()
                .filter(conference -> !conference.isApproved())
                .collect(Collectors.toList());

        // TODO: implement this method
//        return this.conferenceRepository.findAllByApprovedIsFalse();
    }

    public List<Conference> getAllFinishedConferences() {
        return this.daoFactory.createConferenceDao().findAll().stream()
                .filter(Conference::isFinished)
                .collect(Collectors.toList());
        // TODO: implement this method
//        return this.conferenceRepository.findAllByFinishedIsTrue();
    }

//    public List<Conference> getAllNotFinishedConferences() {
//        // TODO: implement this method
////        return this.conferenceRepository.findAllByFinishedIsFalseAndApprovedIsTrue();
//    }

    public List<Conference> getAllConferencesByCurrentUser(HttpServletRequest request) {
        return daoFactory.createConferenceDao().findAll().stream()
                .filter(Conference::isApproved)
                .filter(conference -> !conference.isFinished())
                .filter(conference -> conference.getRegisteredGuests().contains(userService.getCurrentUser(request)))
                .collect(Collectors.toList());

        // TODO: implement this method
//        return this.conferenceRepository.findAllByRegisteredGuestsContainsAndApprovedIsTrueAndFinishedIsFalse(userService.getCurrentUser());
    }

    public Conference getConferenceById(Long id){
        return daoFactory.createConferenceDao().findById(id);
    }

    public void addNewConference(Conference conference) {
        daoFactory.createConferenceDao().create(conference);
    }

    public void deleteConferenceById(Long id) {
        daoFactory.createConferenceDao().delete(id);
    }

    public void updateConferenceById(Long id, Conference newConference) {
        Conference oldConference = daoFactory.createConferenceDao().findById(id);
        copyUpdatableFields(oldConference, newConference);

        daoFactory.createConferenceDao().update(oldConference);
    }

    private void copyUpdatableFields(Conference oldConf, Conference newConf) {

        // TODO null -> @Optional, Object.isNull()

        if (newConf.getTopic() != null) {
            oldConf.setTopic(newConf.getTopic());
        }

        if (newConf.getEventDateTime() != null) {
            oldConf.setEventDateTime(newConf.getEventDateTime());
        }

        if (newConf.getEventAddress() != null) {
            oldConf.setEventAddress(newConf.getEventAddress());
        }

        if (newConf.getDescription() != null) {
            oldConf.setDescription(newConf.getDescription());
        }
    }

    public Set<User> getRegisteredUsers(Long conferenceId) {
        return daoFactory.createConferenceDao()
                .findById(conferenceId)
                .getRegisteredGuests();
    }

    public void changeRegistration(HttpServletRequest request, Long conferenceId) {
        Long userId = userService.getIdOfCurrentUser(request);
        Conference conference = daoFactory.createConferenceDao().findById(conferenceId);
        User user = userService.getUserById(userId);

        if (!conference.isApproved() || conference.isFinished()) {
            throw new ConferenceNotValidException();
        }

        if (!isUserRegistered(request, conference)) {
            conference.getRegisteredGuests().add(user);
        } else {
            conference.getRegisteredGuests().remove(user);
        }

        daoFactory.createConferenceDao().update(conference);
    }

    public boolean isUserRegistered(HttpServletRequest request, Conference conference) {
        Long userId = userService.getIdOfCurrentUser(request);
        return conference.getRegisteredGuests()
                .stream()
                .map(User::getId)
                .collect(Collectors.toSet())
                .contains(userId);
    }

    public void cancelRegistrationOfUser(Long conferenceId, Long userId) {
        Conference conference = daoFactory.createConferenceDao().findById(conferenceId);
        conference.getRegisteredGuests().remove(userService.getUserById(userId));

        daoFactory.createConferenceDao().update(conference);
    }

    public Set<Report> getReportsById(Long conferenceId) {
        return daoFactory.createConferenceDao().findById(conferenceId).getReports();
    }

    public void deleteReport(Long conferenceId, Long reportId) {
        Conference conference = daoFactory.createConferenceDao().findById(conferenceId);
        Report report = reportService.getReportById(reportId);

        conference.getReports().remove(report);

        daoFactory.createConferenceDao().update(conference);
    }

    public void approve(Long conferenceId) {
        Conference conference = daoFactory.createConferenceDao().findById(conferenceId);
        conference.setApproved(true);
        daoFactory.createConferenceDao().update(conference);
    }

    public void reject(Long conferenceId) {
        daoFactory.createConferenceDao().delete(conferenceId);
    }

    public void finish(Long conferenceId, Long numberOfVisitedGuests) {
        Conference conference = daoFactory.createConferenceDao().findById(conferenceId);

        conference.setNumberOfVisitedGuests(numberOfVisitedGuests);
        conference.setFinished(true);

        daoFactory.createConferenceDao().update(conference);
    }
}
