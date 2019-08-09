package ua.dovhopoliuk.model.service;

import ua.dovhopoliuk.model.dao.DaoFactory;
import ua.dovhopoliuk.model.entity.User;
import ua.dovhopoliuk.model.entity.Vote;
import ua.dovhopoliuk.model.exception.VoteNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class VoteService {
    private final DaoFactory daoFactory = DaoFactory.getInstance();
    private UserService userService;

    public VoteService(UserService userService) {
        this.userService = userService;
    }

    public List<Vote> getAllVotesBySpeaker(User speaker) {
        return daoFactory.createVoteDao().findAll().stream()
                .filter(vote -> vote.getSpeaker().equals(speaker))
                .collect(Collectors.toList());
        //TODO: implement this method
//        return voteRepository.findAllBySpeaker(speaker);
    }

    public void saveVote(HttpServletRequest request, User speaker, int mark) {
        User user = userService.getCurrentUser(request);

        Vote currentVote = daoFactory.createVoteDao().findAll().stream()
                .filter(vote -> vote.getSpeaker().equals(speaker) && vote.getUser().equals(user))
                .findFirst()
                .orElseGet(() -> {
                    Vote vote = new Vote();
                    vote.setSpeaker(speaker);
                    vote.setUser(user);
                    return vote;
                });


        currentVote.setMark(mark);

        if (Objects.isNull(currentVote.getId())) {
            daoFactory.createVoteDao().create(currentVote);
        } else {
            daoFactory.createVoteDao().update(currentVote);
        }
    }

    public int getVoteOfCurrentUser(HttpServletRequest request, User speaker) {
        User user = userService.getCurrentUser(request);

        Vote currentVote = daoFactory.createVoteDao().findAll().stream()
                .filter(vote -> vote.getSpeaker().equals(speaker) && vote.getUser().equals(user))
                .findFirst().orElseThrow(VoteNotFoundException::new);
        // TODO: implement this method
//        Vote vote = voteRepository.findBySpeakerAndUser(speaker, userService.getCurrentUser())
//                .orElseThrow(VoteNotFoundException::new);

        return currentVote.getMark();
    }
}
