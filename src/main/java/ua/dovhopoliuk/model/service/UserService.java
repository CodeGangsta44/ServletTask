package ua.dovhopoliuk.model.service;

import ua.dovhopoliuk.model.dao.DaoFactory;
import ua.dovhopoliuk.model.dto.SpeakerStatisticsDTO;
import ua.dovhopoliuk.model.dto.UserDTO;
import ua.dovhopoliuk.model.entity.Conference;
import ua.dovhopoliuk.model.entity.Notification;
import ua.dovhopoliuk.model.entity.Report;
import ua.dovhopoliuk.model.entity.User;
import ua.dovhopoliuk.model.exception.LoginNotUniqueException;
import ua.dovhopoliuk.model.exception.UserNotAuthenticatedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserService {
    private final DaoFactory daoFactory = DaoFactory.getInstance();
    private final ReportService reportService;

    public UserService(ReportService reportService) {
        this.reportService = reportService;
    }

    public List<User> getAllUsers() {
        return daoFactory.createUserDao().findAll();

//        if (users.size() == 0) {
//            String localizedMessage = messageSource.getMessage("exception.user.list.empty",
//                    null,
//                    LocaleContextHolder.getLocale());
//
//            throw new EmptyUserListException("No users is system", localizedMessage);
//        } else {
//            log.info("Returning list of users");
//            return users.stream().map(UserDTO::new).collect(Collectors.toSet());
//        }
    }

    public void saveUser (User user){

        daoFactory.createUserDao().create(user);

//        try {
//            daoFactory.createUserDao().create(user);
////            userRepository.save(user);
//        } catch (Exception ex){
//            Throwable specificException = NestedExceptionUtils.getMostSpecificCause(ex);
//
//            int errorCode = 0;
//
//            if (specificException instanceof SQLException) {
//                SQLException sqlException = (SQLException)specificException;
//                errorCode = sqlException.getErrorCode();
//            }
//
//            if (errorCode == 1062) {
//                log.warn("Login already exists");
//                String localizedMessage = messageSource.getMessage("exception.login.not.unique",
//                        null,
//                        LocaleContextHolder.getLocale());
//
//                throw new LoginNotUniqueException("Entered login is not unique, please try again", localizedMessage);
//            }
//
//            throw ex;
//        }

    }

    public void saveExistingUser(User user) {
        daoFactory.createUserDao().update(user);
    }

    public User getUserById(Long id) {
        return daoFactory.createUserDao().findById(id);
//        return userRepository.findUserById(id).orElse(null);
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.findUserByLogin(username)
//                .orElseThrow(() -> new UsernameNotFoundException(username));
//    }

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
        return daoFactory.createUserDao().findById(getIdOfCurrentUser(request));
    }

    public void updateUser(UserDTO userDTO) {
        User target = daoFactory.createUserDao().findById(userDTO.getId());
        mapSourceToTarget(userDTO, target);
        daoFactory.createUserDao().update(target);
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
        User user = daoFactory.createUserDao().findById(userId);

        List<Report> userReports = daoFactory.createReportDao().findAll().stream()
                .filter(report -> report.getSpeaker().equals(user))
                .collect(Collectors.toList());
        // TODO: implement this method
//        List<Report> userReports = reportRepository.findAllBySpeaker(user);

        userReports.forEach(reportService::deleteReport);

        List<Conference> userConferences = daoFactory.createConferenceDao().findAll().stream()
                .filter(conference -> conference.getRegisteredGuests().contains(user))
                .collect(Collectors.toList());

        // TODO: implement this method
//        List<Conference> userConferences = conferenceRepository.findAllByRegisteredGuestsContains(user);

        userConferences.forEach(conference -> {
            conference.getRegisteredGuests().remove(user);
            daoFactory.createConferenceDao().update(conference);
        });

        List<Notification> userNotifications = daoFactory.createNotificationDao().findAll().stream()
                .filter(notification -> notification.getAddressedUser().equals(user))
                .collect(Collectors.toList());

        userNotifications.forEach(notification -> {
            daoFactory.createNotificationDao().delete(notification.getId());
        });

        // TODO: implement this method
//        notificationRepository.deleteAllByAddressedUser(user);

        daoFactory.createUserDao().delete(user.getId());
    }

    public SpeakerStatisticsDTO getSpeakerStatistics(Long userId) {
        User speaker = daoFactory.createUserDao().findById(userId);

        List<Report> speakerReports = daoFactory.createReportDao().findAll().stream()
                .filter(report -> report.getSpeaker().equals(speaker))
                .collect(Collectors.toList());
//        List<Report> speakerReports = reportRepository.findAllBySpeaker(speaker);

        Set<Conference> speakerConferences = new HashSet<>();

        Set<Report> completedSpeakerReports = speakerReports.stream()
                .filter(report -> report.getConference().isFinished())
                .peek(report -> speakerConferences.add(report.getConference()))
                .collect(Collectors.toSet());

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
        daoFactory.createUserDao().create(user);
    }

    public User getUserByLogin(String login) {
        return daoFactory.createUserDao().findByLogin(login);
    }
}
