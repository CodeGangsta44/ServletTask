package ua.dovhopoliuk.model.service;

import ua.dovhopoliuk.model.dao.DaoFactory;
import ua.dovhopoliuk.model.dao.VoteDao;
import ua.dovhopoliuk.model.entity.User;
import ua.dovhopoliuk.model.entity.Vote;
import ua.dovhopoliuk.model.exception.VoteNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

public class VoteService {
    private final DaoFactory daoFactory = DaoFactory.getInstance();
    private UserService userService;

    public VoteService(UserService userService) {
        this.userService = userService;
    }

    public List<Vote> getAllVotesBySpeaker(Long speakerId) {
        try (VoteDao voteDao = daoFactory.createVoteDao()) {
            return voteDao.findAllBySpeakerId(speakerId);
        }
    }

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
