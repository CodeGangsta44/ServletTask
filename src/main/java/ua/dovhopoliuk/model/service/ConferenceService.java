package ua.dovhopoliuk.model.service;

import ua.dovhopoliuk.model.dao.ConferenceDao;
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

    public ConferenceService(UserService userService) {

        this.userService = userService;
    }

    public List<Conference> getAllValidConferences(){
        try (ConferenceDao conferenceDao = daoFactory.createConferenceDao()) {
            return conferenceDao.findAllByApprovedIsTrueAndFinishedIsFalse();
        }
    }

    public List<Conference> getAllValidConferencesByPage(Integer page, Integer capacity) {
        Integer start = (page - 1) * capacity;
        Integer end = capacity;
        try (ConferenceDao conferenceDao = daoFactory.createConferenceDao()) {
            return conferenceDao.findAllByApprovedIsTrueAndFinishedIsFalse(start, end);
        }
    }

    public List<Conference> getAllNotApprovedConferences()  {
        try (ConferenceDao conferenceDao = daoFactory.createConferenceDao()) {
            return conferenceDao.findAllByApprovedIsFalse();
        }
    }

    public List<Conference> getAllFinishedConferences() {
        try (ConferenceDao conferenceDao = daoFactory.createConferenceDao()) {
            return conferenceDao.findAllByFinishedIsTrue();
        }
    }


    public List<Conference> getAllConferencesByCurrentUser(HttpServletRequest request) {
        try (ConferenceDao conferenceDao = daoFactory.createConferenceDao()) {
            return conferenceDao
                    .findAllByRegisteredGuestsContainsAndApprovedIsTrueAndFinishedIsFalse(userService
                            .getIdOfCurrentUser(request));
        }
    }

    public Conference getConferenceById(Long id){
        try (ConferenceDao conferenceDao = daoFactory.createConferenceDao()) {
            return conferenceDao.findById(id);
        }
    }

    public Integer getTotalNumberOfValidConferences() {
        try (ConferenceDao conferenceDao = daoFactory.createConferenceDao()) {
            return conferenceDao.getTotalNumberOfConferencesByApprovedIsTrueAndFinishedIsFalse();
        }
    }

    public void addNewConference(Conference conference) {
        try (ConferenceDao conferenceDao = daoFactory.createConferenceDao()) {
            conferenceDao.create(conference);
        }
    }

    public void deleteConferenceById(Long id) {
        try (ConferenceDao conferenceDao = daoFactory.createConferenceDao()) {
            conferenceDao.delete(id);
        }
    }

    public void updateConferenceById(Conference newConference) {
//        Conference oldConference = getConferenceById(id);
//        copyUpdatableFields(oldConference, newConference);

        try (ConferenceDao conferenceDao = daoFactory.createConferenceDao()) {
            conferenceDao.update(newConference);
        }
    }

    private void copyUpdatableFields(Conference oldConf, Conference newConf) {

//        // TODO null -> @Optional, Object.isNull()
//
//        if (newConf.getTopic() != null) {
//            oldConf.setTopic(newConf.getTopic());
//        }
//
//        if (newConf.getEventDateTime() != null) {
//            oldConf.setEventDateTime(newConf.getEventDateTime());
//        }
//
//        if (newConf.getEventAddress() != null) {
//            oldConf.setEventAddress(newConf.getEventAddress());
//        }
//
//        if (newConf.getDescription() != null) {
//            oldConf.setDescription(newConf.getDescription());
//        }
    }

    public Set<User> getRegisteredUsers(Long conferenceId) {
        return getConferenceById(conferenceId).getRegisteredGuests();
    }

    public void changeRegistration(HttpServletRequest request, Long conferenceId) {

        Long userId = userService.getIdOfCurrentUser(request);
        Conference conference = getConferenceById(conferenceId);
        User user = userService.getUserById(userId);

        if (!conference.isApproved() || conference.isFinished()) {
            throw new ConferenceNotValidException();
        }

        if (!isUserRegistered(request, conference)) {
            conference.getRegisteredGuests().add(user);
        } else {
            conference.getRegisteredGuests().remove(user);
        }

        try (ConferenceDao conferenceDao = daoFactory.createConferenceDao()) {
            conferenceDao.update(conference);
        }
    }

    public boolean isUserRegistered(HttpServletRequest request, Conference conference) {
        Long userId = userService.getIdOfCurrentUser(request);
        return conference.getRegisteredGuests()
                .stream()
                .map(User::getId)
                .collect(Collectors.toSet())
                .contains(userId);
    }


    public Set<Report> getReportsById(Long conferenceId) {
        return getConferenceById(conferenceId).getReports();
    }


    public void approve(Long conferenceId) {
        Conference conference = getConferenceById(conferenceId);
        conference.setApproved(true);
        updateConferenceById(conference);
    }

    public void reject(Long conferenceId) {
        deleteConferenceById(conferenceId);
    }

    public void finish(Long conferenceId, Long numberOfVisitedGuests) {
        Conference conference = getConferenceById(conferenceId);
        conference.setNumberOfVisitedGuests(numberOfVisitedGuests);
        conference.setFinished(true);
        updateConferenceById(conference);
    }
}
