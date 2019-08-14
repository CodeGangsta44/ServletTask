package ua.dovhopoliuk.model.service;

import ua.dovhopoliuk.model.dao.DaoFactory;
import ua.dovhopoliuk.model.dao.VoteDao;
import ua.dovhopoliuk.model.entity.User;
import ua.dovhopoliuk.model.entity.Vote;
import ua.dovhopoliuk.model.exception.VoteNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * Class that is a votes service implementation.
 * Using to access the votes DAO, such as:
 * getting all the votes for the speaker,
 * saving and getting the vote of the current user
 *
 * @author Roman Dovhopoliuk
 * @version 1.0.0
 */
public class VoteService {
    /** Attribute daoFactory for creating instances of entities`s DAOs
     * @see DaoFactory
     */
    private final DaoFactory daoFactory = DaoFactory.getInstance();

    /** Attribute service of users
     * @see UserService
     */
    private UserService userService;

    /**
     * Constructor for creating new vote service
     * @param userService - service for getting access for methods, referenced to User domain
     */
    public VoteService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Method for getting all votes of chosen speaker by id
     * @see User
     * @see ua.dovhopoliuk.model.entity.Role
     * @param speakerId - chosen speaker
     * @return list of all votes by chosen speaker
     * @see List
     */
    public List<Vote> getAllVotesBySpeaker(Long speakerId) {
        try (VoteDao voteDao = daoFactory.createVoteDao()) {
            return voteDao.findAllBySpeakerId(speakerId);
        }
    }

    /**
     * Method for saving vote(mark) of chosen speaker by current user
     * @see User
     * @see ua.dovhopoliuk.model.entity.Role
     * @param speakerId - if of chosen speaker
     * @param mark - mark for chosen speaker by current user
     * @param request - entity with additional information about current session
     * @see HttpServletRequest
     */
    public void saveVote(HttpServletRequest request, Long speakerId, int mark) {
        User speaker = userService.getUserById(speakerId);
        User user = userService.getCurrentUser(request);

        Vote currentVote;

        try (VoteDao voteDao = daoFactory.createVoteDao()) {
            currentVote = voteDao.findBySpeakerAndUser(speakerId, user.getId())
                    .orElseGet(() -> {
                        Vote vote = new Vote();
                        vote.setSpeaker(speaker);
                        vote.setUser(user);
                        return vote;
            });
        }

        currentVote.setMark(mark);

        try (VoteDao voteDao = daoFactory.createVoteDao()) {
            if (Objects.isNull(currentVote.getId())) {
                voteDao.create(currentVote);
            } else {
                voteDao.update(currentVote);
            }
        }
    }

    /**
     * Method for getting vote(mark) of chosen speaker by current user
     * @see User
     * @see ua.dovhopoliuk.model.entity.Role
     * @param speakerId - id of chosen speaker
     * @param request - entity with additional information about current session
     * @see HttpServletRequest
     * @return - mark for chosen speaker by current user
     */
    public int getVoteOfCurrentUser(HttpServletRequest request, Long speakerId) {
        User user = userService.getCurrentUser(request);

        Vote currentVote;

        try (VoteDao voteDao = daoFactory.createVoteDao()) {
            currentVote = voteDao.findBySpeakerAndUser(speakerId, user.getId())
                    .orElseThrow(VoteNotFoundException::new);
        }

        return currentVote.getMark();
    }
}
