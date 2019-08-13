package ua.dovhopoliuk.model.service;

import ua.dovhopoliuk.model.dao.DaoFactory;
import ua.dovhopoliuk.model.dao.ReportDao;
import ua.dovhopoliuk.model.dao.UserDao;
import ua.dovhopoliuk.model.dto.SpeakerStatisticsDTO;
import ua.dovhopoliuk.model.dto.UserDTO;
import ua.dovhopoliuk.model.entity.Conference;
import ua.dovhopoliuk.model.entity.Report;
import ua.dovhopoliuk.model.entity.User;
import ua.dovhopoliuk.model.exception.UserNotAuthenticatedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserService {
    private final DaoFactory daoFactory = DaoFactory.getInstance();

    public List<User> getAllUsers() {
        try (UserDao userDao = daoFactory.createUserDao()) {
            return userDao.findAll();
        }
    }

    public User getUserById(Long id) {
        try (UserDao userDao = daoFactory.createUserDao()) {
            return userDao.findById(id);
        }
    }

    public Long getIdOfCurrentUser(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            User user = (User)session.getAttribute("principals");
            return user.getId();
        } catch (RuntimeException e) {
            throw new UserNotAuthenticatedException();
        }
    }

    public User getCurrentUser(HttpServletRequest request) {
        return getUserById(getIdOfCurrentUser(request));
    }

    public void updateUser(UserDTO userDTO) {
        User target = getUserById(userDTO.getId());
        mapSourceToTarget(userDTO, target);

        System.out.println(target.getRoles());

        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.update(target);
        }
    }

    private void mapSourceToTarget(UserDTO source, User target) {
        target.setSurname(source.getSurname());
        target.setName(source.getName());
        target.setPatronymic(source.getPatronymic());
        target.setLogin(source.getLogin());
        target.setEmail(source.getEmail());
        target.setRoles(source.getRoles());
    }

    public void deleteUser(Long userId) {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.delete(userId);
        }
    }

    public SpeakerStatisticsDTO getSpeakerStatistics(Long userId) {
        List<Report> completedSpeakerReports;

        try (ReportDao reportDao = daoFactory.createReportDao()) {
            completedSpeakerReports = reportDao.findAllBySpeakerIdAndConferenceIsFinished(userId);
        }

        Set<Conference> speakerConferences = new HashSet<>();

        completedSpeakerReports.forEach(report ->  speakerConferences.add(report.getConference()));

        Long totalReports = (long) completedSpeakerReports.size();
        Long totalConferences = (long) speakerConferences.size();
        Long totalPeople = speakerConferences.stream()
                .reduce(0L, (left, right) -> left + right.getNumberOfVisitedGuests(), Long::sum);

        SpeakerStatisticsDTO speakerStatisticsDTO = new SpeakerStatisticsDTO();

        speakerStatisticsDTO.setTotalReports(totalReports);
        speakerStatisticsDTO.setTotalConferences(totalConferences);
        speakerStatisticsDTO.setTotalPeople(totalPeople);

        return  speakerStatisticsDTO;
    }

    public void registerUser(User user) {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.create(user);
        }
    }

    public User getUserByLogin(String login) {
        try (UserDao userDao = daoFactory.createUserDao()) {
            return userDao.findByLogin(login);
        }
    }
}
